package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.RetweetRequestDto;
import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.dto.response.RetweetActionResponseDto;
import com.myproject.twitter.dto.response.RetweetUserResponseDto;
import com.myproject.twitter.entity.Retweet;
import com.myproject.twitter.entity.Tweet;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.exception.RetweetConflictException;
import com.myproject.twitter.exception.RetweetNotFoundException;
import com.myproject.twitter.exception.TweetNotFoundException;
import com.myproject.twitter.repository.RetweetRepository;
import com.myproject.twitter.repository.TweetRepository;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.security.CustomUserDetails;
import com.myproject.twitter.util.mapper.RetweetMapper;
import com.myproject.twitter.util.pagination.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final AuthUtils authUtils;

    private final RetweetRepository retweetRepository;

    private final TweetRepository tweetRepository;

    private final FollowService followService;

    private final RetweetMapper retweetMapper;

    @Override
    @Transactional
    public RetweetActionResponseDto create(Long tweetId, RetweetRequestDto retweetRequestDto) {

        User userReference = authUtils.getCurrentUserReference();

        Long currentUserId = userReference.getId();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Retweet için tweet bulunamadı"));

        if (retweetRepository.existsByUserIdAndTweetId( currentUserId, tweet.getId())) {
            throw new RetweetConflictException("Bu tweeti zaten retweet yaptınız!");
        }

        Retweet retweet = retweetMapper.toEntity(retweetRequestDto);

        retweet.setUser(userReference);

        retweet.setTweet(tweet);

        Retweet savedRetweet = retweetRepository.save(retweet);

        return retweetMapper.toResponseDto(savedRetweet, true);

    }

    @Override
    @Transactional
    public void deleteByTweetId(Long tweetId) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        Long currentUserId = currentUser.getId();

        if (!tweetRepository.existsById(tweetId)) {
            throw new TweetNotFoundException("Retweet için tweet bulunamadı");
        }

        if (!retweetRepository.existsByUserIdAndTweetId( currentUserId, tweetId)) {
            throw new RetweetNotFoundException("Bu tweet için aktif bir retweet bulunamadı");
        }

        retweetRepository.deleteByUserIdAndTweetId( currentUserId, tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<RetweetUserResponseDto> getRetweetersByTweetId(Long tweetId, String cursor, int size) {

        if (!tweetRepository.existsById(tweetId)) {
            throw new TweetNotFoundException("Tweet bulunamadı: " + tweetId);
        }

        Long currentUserId = authUtils.getCurrentUserReference().getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);

        Pageable pageable = PageRequest.of(0, size + 1);

        List<Retweet> retweets;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {
            retweets = retweetRepository.findFirstPageByTweetId(tweetId, pageable);
        } else {
            retweets = retweetRepository.findNextPageByTweetId(tweetId, cursorData.cursorDate(), cursorData.lastId(), pageable);
        }

        boolean hasNext = retweets.size() > size;
        List<Retweet> content = hasNext ? new ArrayList<>(retweets.subList(0, size)) : retweets;

        String nextCursor = null;
        if (!content.isEmpty() && hasNext) {
            Retweet lastItem = content.get(content.size() - 1);
            nextCursor = CursorUtils.encode(lastItem.getCreatedAt(), lastItem.getId());
        }

        List<RetweetUserResponseDto> dtoList = content.stream()
                .map(retweet -> {
                    boolean isFollowing = followService.isFollowing(currentUserId, retweet.getUser().getId());
                    return retweetMapper.toUserResponseDto(retweet, isFollowing);
                })
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRetweetedByUser(Long tweetId, Long userId) {

        return retweetRepository.existsByUserIdAndTweetId(userId, tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countRetweets(Long tweetId) {

        return retweetRepository.countByTweetId(tweetId);
    }
}

package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.dto.response.LikeActionResponseDto;
import com.myproject.twitter.dto.response.LikeUserResponseDto;
import com.myproject.twitter.entity.Like;
import com.myproject.twitter.entity.Tweet;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.exception.LikeConflictException;
import com.myproject.twitter.exception.LikeNotFoundException;
import com.myproject.twitter.exception.TweetNotFoundException;
import com.myproject.twitter.repository.LikeRepository;
import com.myproject.twitter.repository.TweetRepository;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.security.CustomUserDetails;
import com.myproject.twitter.util.mapper.LikeMapper;
import com.myproject.twitter.util.pagination.CursorUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final AuthUtils authUtils;

    private final LikeRepository likeRepository;

    private final TweetRepository tweetRepository;

    private final FollowService followService;

    private final LikeMapper likeMapper;

    @Override
    @Transactional
    public LikeActionResponseDto like(Long tweetId) {

        User userReference = authUtils.getCurrentUserReference();

        Long currentUserId = userReference.getId();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Like için tweet bulunamadı"));

        if (likeRepository.existsByUserIdAndTweetId( currentUserId, tweet.getId())) {
            throw new LikeConflictException("Bu tweeti zaten like yaptınız!");
        }

        Like like = new Like();
        like.setUser(userReference);
        like.setTweet(tweet);

        Like savedLike = likeRepository.save(like);

        return likeMapper.toResponseDto(savedLike, true);
    }

    @Override
    @Transactional
    public LikeActionResponseDto unlike(Long tweetId) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        Long currentUserId = currentUser.getId();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı"));

        Like existingLike = likeRepository.findByUserIdAndTweetId(currentUserId, tweet.getId())
                .orElseThrow(() -> new LikeNotFoundException("Bu tweet için aktif bir beğeniniz bulunamadı!"));

        LikeActionResponseDto responseDto = likeMapper.toResponseDto(existingLike, false);

        likeRepository.delete(existingLike);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<LikeUserResponseDto> getLikesByTweetId(Long tweetId, String cursor, int size) {

        if (!tweetRepository.existsById(tweetId)) {
            throw new TweetNotFoundException("Tweet bulunamadı: " + tweetId);
        }

        Long currentUserId = authUtils.getCurrentUserReference().getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);

        Pageable pageable = PageRequest.of(0, size + 1);

        List<Like> likes;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {
            likes = likeRepository.findFirstPageByTweetId(tweetId, pageable);
        } else {
            likes = likeRepository.findNextPageByTweetId(tweetId, cursorData.cursorDate(), cursorData.lastId(), pageable);
        }

        boolean hasNext = likes.size() > size;
        List<Like> content = hasNext ? new ArrayList<>(likes.subList(0, size)) : likes;

        String nextCursor = null;
        if (!content.isEmpty() && hasNext) {
            Like lastItem = content.get(content.size() - 1);
            nextCursor = CursorUtils.encode(lastItem.getCreatedAt(), lastItem.getId());
        }

        List<LikeUserResponseDto> dtoList = content.stream()
                .map(like -> {
                    boolean isFollowing = followService.isFollowing(currentUserId, like.getUser().getId());
                    return likeMapper.toUserResponseDto(like, isFollowing);
                })
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLikedByUser(Long tweetId, Long userId) {

        return likeRepository.existsByUserIdAndTweetId(userId, tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLikes(Long tweetId) {

        return likeRepository.countByTweetId(tweetId);
    }

}

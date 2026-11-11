package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.BookmarkResponseDto;
import com.myproject.twitter.entity.Bookmark;
import com.myproject.twitter.entity.Tweet;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.exception.*;
import com.myproject.twitter.repository.BookmarkRepository;
import com.myproject.twitter.repository.TweetRepository;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.security.CustomUserDetails;
import com.myproject.twitter.util.mapper.BookmarkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService{

    private final AuthUtils authUtils;

    private final BookmarkRepository bookmarkRepository;

    private final TweetRepository tweetRepository;

    private final BookmarkMapper bookmarkMapper;

    @Override
    @Transactional
    public BookmarkResponseDto create(Long tweetId) {

        User userReference = authUtils.getCurrentUserReference();

        Long currentUserId = userReference.getId();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Bookmark için tweet bulunamadı"));

        if (bookmarkRepository.existsByUserIdAndTweetId(currentUserId, tweet.getId())) {
            throw new BookmarkConflictException("Bu tweeti zaten bookmarked yaptınız!");
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(userReference);
        bookmark.setTweet(tweet);

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return bookmarkMapper.toResponseDto(savedBookmark, true);

    }

    @Override
    @Transactional
    public void deleteByTweetId(Long tweetId) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        Long currentUserId = currentUser.getId();

        if (!tweetRepository.existsById(tweetId)) {
            throw new TweetNotFoundException("Bookmark için tweet bulunamadı");
        }

        if (!bookmarkRepository.existsByUserIdAndTweetId( currentUserId, tweetId)) {
            throw new BookmarkNotFoundException("Bu tweet için aktif bir bookmark bulunamadı");
        }

        bookmarkRepository.deleteByUserIdAndTweetId( currentUserId, tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarkedByUser(Long tweetId, Long userId) {

        return bookmarkRepository.existsByUserIdAndTweetId(userId, tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countBookmarks(Long tweetId) {

        return bookmarkRepository.countByTweetId(tweetId);
    }
}

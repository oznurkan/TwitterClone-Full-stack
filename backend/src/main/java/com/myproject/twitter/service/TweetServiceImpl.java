package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.TweetPatchRequestDto;
import com.myproject.twitter.dto.request.TweetRequestDto;
import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.dto.response.TweetResponseDto;
import com.myproject.twitter.entity.*;
import com.myproject.twitter.exception.TweetNotFoundException;
import com.myproject.twitter.repository.*;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.security.CustomUserDetails;
import com.myproject.twitter.util.helper.TweetEnricher;
import com.myproject.twitter.util.mapper.TweetMapper;
import com.myproject.twitter.util.pagination.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final AuthUtils authUtils;

    private final TweetRepository tweetRepository;

    private final TweetMapper tweetMapper;

    private final TweetEnricher tweetEnricher;

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<TweetResponseDto> getFeedTweets(String cursor, int size) {
        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Tweet> tweets;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {
            tweets = tweetRepository.findFirstPage(pageRequest);
        } else {
            tweets = tweetRepository.findNextPage(cursorData.cursorDate(), cursorData.lastId(), pageRequest);
        }

        boolean hasNext = tweets.size() > size;
        List<Tweet> pageContent = hasNext ? new ArrayList<>(tweets.subList(0, size)) : tweets;

        String nextCursor = null;
        if (hasNext && !pageContent.isEmpty()) {
            Tweet lastTweet = pageContent.get(pageContent.size() - 1);
            nextCursor = CursorUtils.encode(lastTweet.getCreatedAt(), lastTweet.getId());
        }

        List<TweetResponseDto> dtoList = pageContent.stream()
                .map(tweet -> tweetEnricher.enrich(tweet, currentUserId))
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public TweetResponseDto findById(Long id) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı, id: " + id));

        return tweetEnricher.enrich(tweet, currentUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<TweetResponseDto> searchTweetByContent(String text, String cursor, int size) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Tweet> tweets;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {
            tweets = tweetRepository.searchFirstPageByContent(text, pageRequest);
        } else {
            tweets = tweetRepository.searchNextPageByContent(text, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
        }

        boolean hasNext = tweets.size() > size;
        List<Tweet> pageContent = hasNext ? new ArrayList<>(tweets.subList(0, size)) : tweets;

        String nextCursor = null;
        if (hasNext && !pageContent.isEmpty()) {
            Tweet lastTweet = pageContent.get(pageContent.size() - 1);
            nextCursor = CursorUtils.encode(lastTweet.getCreatedAt(), lastTweet.getId());
        }

        List<TweetResponseDto> dtoList = pageContent.stream()
                .map(tweet -> tweetEnricher.enrich(tweet, currentUserId))
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional
    public TweetResponseDto create(TweetRequestDto tweetRequestDto) {

        authUtils.getRequiredCurrentUserDetails();

        User userReference = authUtils.getCurrentUserReference();

        Tweet tweet = tweetMapper.toEntity(tweetRequestDto);

        tweet.setUser(userReference);

        Tweet savedTweet = tweetRepository.save(tweet);

        return tweetMapper.toResponseDto(savedTweet, 0L, 0L, 0L, 0L, false, false, false);
    }

    @Override
    @Transactional
    public TweetResponseDto update(Long tweetId, TweetPatchRequestDto tweetPatchRequestDto) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        Tweet tweetToUpdate = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı, id: " + tweetId));

        authUtils.verifyOwnership(tweetToUpdate.getUser().getId());

        tweetMapper.updateEntity(tweetToUpdate, tweetPatchRequestDto);

        Tweet savedTweet = tweetRepository.save(tweetToUpdate);

        return tweetEnricher.enrich(savedTweet, currentUser.getId());
    }

    @Override
    @Transactional
    public TweetResponseDto replaceOrCreate(Long tweetId, TweetRequestDto tweetRequestDto) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        User userReference = authUtils.getCurrentUserReference();

        Tweet tweetToSave = tweetRepository.findById(tweetId)
                .map(existingTweet -> {
                    authUtils.verifyOwnership(existingTweet.getUser().getId());
                    existingTweet.setContent(tweetRequestDto.content());
                    return existingTweet;
                })
                .orElseGet(() -> {
                    Tweet newTweet = tweetMapper.toEntity(tweetRequestDto);
                    newTweet.setUser(userReference);
                    return newTweet;
                });

        Tweet savedTweet = tweetRepository.save(tweetToSave);

        return tweetEnricher.enrich(savedTweet, currentUser.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long tweetId) {

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı , id:" + tweetId));

        authUtils.verifyOwnership(tweet.getUser().getId());

        tweetRepository.delete(tweet);
    }

}
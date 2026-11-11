package com.myproject.twitter.service;

import com.myproject.twitter.dto.internal.PostItem;
import com.myproject.twitter.dto.response.*;
import com.myproject.twitter.entity.*;
import com.myproject.twitter.repository.*;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.security.CustomUserDetails;
import com.myproject.twitter.util.helper.TweetEnricher;
import com.myproject.twitter.util.pagination.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final AuthUtils authUtils;

    private final TweetRepository tweetRepository;

    private final CommentRepository commentRepository;

    private final RetweetRepository retweetRepository;

    private final LikeRepository likeRepository;

    private final BookmarkRepository bookmarkRepository;

    private final UserService userService;

    private final TweetEnricher tweetEnricher;

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<UserProfilePostResponseDto> getUserPosts(String nickname, String cursor, int size) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        Long targetUserId = userService.getEntityByNickName(nickname).getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Tweet> tweets;
        List<Retweet> retweets;

        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {

            tweets = tweetRepository.findFirstPageByUserId(targetUserId, pageRequest);
            retweets = retweetRepository.findFirstPageByUserId(targetUserId, pageRequest);
        } else {

            tweets = tweetRepository.findNextPageByUserId(targetUserId, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
            retweets = retweetRepository.findNextPageByUserId(targetUserId, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
        }

        List<PostItem> combinedList = new ArrayList<>();

        for (Tweet t : tweets) {
            TweetResponseDto tweetDto = tweetEnricher.enrich(t, currentUserId);
            UserProfilePostResponseDto postDto = new UserProfilePostResponseDto(
                    t.getId(),
                    "TWEET",
                    null,
                    tweetDto,
                    t.getCreatedAt()
            );
            combinedList.add(new PostItem(t.getCreatedAt(), t.getId(), postDto));
        }

        for (Retweet r : retweets) {
            TweetResponseDto originalTweetDto = tweetEnricher.enrich(r.getTweet(), currentUserId);
            UserProfilePostResponseDto postDto = new UserProfilePostResponseDto(
                    r.getId(),
                    "RETWEET",
                    r.getText(),
                    originalTweetDto,
                    r.getCreatedAt()
            );
            combinedList.add(new PostItem(r.getCreatedAt(), r.getId(), postDto));
        }

        combinedList.sort(
                Comparator.comparing(PostItem::createdAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Comparator.comparing(PostItem::id, Comparator.nullsLast(Comparator.reverseOrder())))
        );

        boolean hasNext = combinedList.size() > size;
        List<PostItem> pageContent = hasNext ? new ArrayList<>(combinedList.subList(0, size)) : combinedList;

        String nextCursor = null;
        if (hasNext && !pageContent.isEmpty()) {
            PostItem lastItem = pageContent.get(pageContent.size() - 1);
            nextCursor = CursorUtils.encode(lastItem.createdAt(), lastItem.id());
        }

        List<UserProfilePostResponseDto> dtoList = pageContent.stream()
                .map(PostItem::dto)
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<TweetResponseDto> getUserTweets(String nickname, String cursor, int size) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        Long targetUserId = userService.getEntityByNickName(nickname).getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Tweet> tweets;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {

            tweets = tweetRepository.findFirstPageByUserId(targetUserId, pageRequest);
        } else {

            tweets = tweetRepository.findNextPageByUserId(targetUserId, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
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
    public CursorPageResponseDto<UserProfileCommentResponseDto> getUserComments(String nickname, String cursor, int size) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        Long targetUserId = userService.getEntityByNickName(nickname).getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Comment> comments;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {

            comments = commentRepository.findFirstPageByUserId(targetUserId, pageRequest);
        } else {

            comments = commentRepository.findNextPageByUserId(targetUserId, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
        }

        boolean hasNext = comments.size() > size;
        List<Comment> pageContent = hasNext ? new ArrayList<>(comments.subList(0, size)) : comments;

        String nextCursor = null;
        if (hasNext && !pageContent.isEmpty()) {
            Comment lastComment = pageContent.get(pageContent.size() - 1);
            nextCursor = CursorUtils.encode(lastComment.getCreatedAt(), lastComment.getId());
        }

        List<UserProfileCommentResponseDto> dtoList = pageContent.stream()
                .map(comment -> new UserProfileCommentResponseDto(
                        comment.getUser().getNickName(),
                        comment.getContent(),
                        tweetEnricher.enrich(comment.getTweet(), currentUserId)
                ))
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<UserProfileRetweetResponseDto> getUserRetweets(String nickname, String cursor, int size) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        Long targetUserId = userService.getEntityByNickName(nickname).getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Retweet> retweets;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {

            retweets = retweetRepository.findFirstPageByUserId(targetUserId, pageRequest);
        } else {

            retweets = retweetRepository.findNextPageByUserId(targetUserId, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
        }

        boolean hasNext = retweets.size() > size;
        List<Retweet> pageContent = hasNext ? new ArrayList<>(retweets.subList(0, size)) : retweets;

        String nextCursor = null;
        if (hasNext && !pageContent.isEmpty()) {

            Retweet lastRetweet = pageContent.get(pageContent.size() - 1);
            nextCursor = CursorUtils.encode(lastRetweet.getCreatedAt(), lastRetweet.getId());
        }

        List<UserProfileRetweetResponseDto> dtoList = pageContent.stream()
                .map(retweet -> new UserProfileRetweetResponseDto(
                        retweet.getUser().getNickName(),
                        retweet.getText(),
                        tweetEnricher.enrich(retweet.getTweet(), currentUserId)
                ))
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<TweetResponseDto> getUserLikedTweets(String nickname, String cursor, int size) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        Long targetUserId = userService.getEntityByNickName(nickname).getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);

        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Like> likes;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {

            likes = likeRepository.findFirstPageByUserId(targetUserId, pageRequest);
        } else {

            likes = likeRepository.findNextPageByUserId(targetUserId, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
        }

        boolean hasNext = likes.size() > size;

        List<Like> pageContent = hasNext ? new ArrayList<>(likes.subList(0, size)) : likes;

        String nextCursor = null;

        if (hasNext && !pageContent.isEmpty()) {

            Like lastLike = pageContent.get(pageContent.size() - 1);
            nextCursor = CursorUtils.encode(lastLike.getCreatedAt(), lastLike.getId());
        }

        List<TweetResponseDto> dtoList = pageContent.stream()
                .map(like -> tweetEnricher.enrich(like.getTweet(), currentUserId))
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<TweetResponseDto> getUserBookmarkedTweets(String cursor, int size) {
        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        List<Bookmark> bookmarks;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {
            bookmarks = bookmarkRepository.findFirstPageByUserId(currentUserId, pageRequest);
        } else {
            bookmarks = bookmarkRepository.findNextPageByUserId(currentUserId, cursorData.cursorDate(), cursorData.lastId(), pageRequest);
        }

        boolean hasNext = bookmarks.size() > size;
        List<Bookmark> pageContent = hasNext ? new ArrayList<>(bookmarks.subList(0, size)) : bookmarks;

        String nextCursor = null;
        if (hasNext && !pageContent.isEmpty()) {
            Bookmark lastBookmark = pageContent.get(pageContent.size() - 1);
            nextCursor = CursorUtils.encode(lastBookmark.getCreatedAt(), lastBookmark.getId());
        }

        List<TweetResponseDto> dtoList = pageContent.stream()
                .map(bookmark -> tweetEnricher.enrich(bookmark.getTweet(), currentUserId))
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }
}
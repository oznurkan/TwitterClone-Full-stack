package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.response.BookmarkResponseDto;
import com.myproject.twitter.entity.Bookmark;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

    public BookmarkResponseDto toResponseDto(Bookmark bookmark, Boolean isBookmarked){

        if (bookmark == null) {
            return null;
        }

        String nickName = (bookmark.getUser() != null) ? bookmark.getUser().getNickName() : null;
        Long tweetId = (bookmark.getTweet() != null) ? bookmark.getTweet().getId() : null;

        return new BookmarkResponseDto(
                nickName,
                tweetId,
                isBookmarked,
                bookmark.getCreatedAt()
        );

    }

}

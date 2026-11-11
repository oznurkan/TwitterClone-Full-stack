package com.myproject.twitter.dto.response;

import java.util.List;

public record CursorPageResponseDto<T>(

        List<T> content,
        String nextCursor,
        boolean hasNext
) {
}

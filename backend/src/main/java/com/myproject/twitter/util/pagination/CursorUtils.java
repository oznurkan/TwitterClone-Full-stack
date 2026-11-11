package com.myproject.twitter.util.pagination;

import java.time.LocalDateTime;

public class CursorUtils {

    private static final String DELIMITER = "_";

    public static String encode(LocalDateTime createdAt, Long id) {
        if (createdAt == null || id == null) return null;
        return createdAt.toString() + DELIMITER + id;
    }

    public static CursorData decode(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return new CursorData(null, null);
        }
        try {
            String[] parts = cursor.split(DELIMITER);
            LocalDateTime cursorDate = LocalDateTime.parse(parts[0]);
            Long lastId = Long.parseLong(parts[1]);
            return new CursorData(cursorDate, lastId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Geçersiz cursor formatı: " + cursor);
        }
    }

    public record CursorData(LocalDateTime cursorDate, Long lastId) {}
}
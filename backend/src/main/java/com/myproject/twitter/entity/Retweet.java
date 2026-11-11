package com.myproject.twitter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Table(name = "retweets", schema = "twitter", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "tweet_id"}))
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString( exclude = { "user", "tweet"})
@EqualsAndHashCode(of = "id")
public class Retweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Size(max = 255, message = "Yorum en fazla 255 karakter olabilir.")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull(message = "Retweet işlemi için kullanıcı olmalıdır")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id")
    @NotNull(message = "Retweet işlemi için tweet olmalıdır")
    private Tweet tweet;

}

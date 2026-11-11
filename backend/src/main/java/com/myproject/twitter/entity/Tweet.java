package com.myproject.twitter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "tweets", schema = "twitter")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"user", "likes", "comments", "retweets", "bookmarks", "hashtags"})
@EqualsAndHashCode(of = "id")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tweet içeriği boş bırakılamaz.")
    @Size(max = 255, message = "Tweet içeriği en fazla 255 karakter olmalıdır")
    private String content;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull(message = "Tweet için kullanıcı alanı boş bırakılamaz")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private Set<Retweet> retweets = new HashSet<>();

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tweet_hashtag",
            schema = "twitter",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();


    public void addLike(Like like){

        this.likes.add(like);
    }

    public void deleteLike(Like like){

        this.likes.remove(like);
    }

    public void addComment(Comment comment){

        this.comments.add(comment);
    }

    public void deleteComment(Comment comment){

        this.comments.remove(comment);
    }

    public void addRetweet(Retweet retweet){

        this.retweets.add(retweet);
    }

    public void deleteRetweet(Retweet retweet){

        this.retweets.remove(retweet);
    }

    public void addBookmark(Bookmark bookmark){

        this.bookmarks.add(bookmark);
    }

    public void deleteBookmark(Bookmark bookmark){

        this.bookmarks.remove(bookmark);
    }




}

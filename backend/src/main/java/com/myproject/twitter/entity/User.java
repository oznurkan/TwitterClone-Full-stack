package com.myproject.twitter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users", schema = "twitter")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString( exclude = { "password", "tweets", "comments", "retweets", "likes", "bookmarks", "followings", "followers", "sentMessages", "receivedMessages"})
@EqualsAndHashCode(of = "id")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name", unique = true, nullable = false)
    @NotBlank(message = "Kullanıcı ismi boş bırakılamaz.")
    @Size(max = 150 , min = 3)
    private String nickName;

    @Column(name = "first_name")
    @NotBlank(message = "Kullanıcı ismi boş bırakılamaz.")
    @Size(max = 100, min = 2)
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Kullanıcı ismi boş bırakılamaz.")
    @Size(max = 100, min = 2)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email alanı boş bırakılamaz.")
    @Size(max = 255 )
    private String email;

    @NotBlank(message = "Şifre alanı boş bırakılamaz")
    @Size(max = 150 , min = 6)
    private String password;

    @Size(max = 255 )
    @Column(name = "bio")
    private String bio;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Tweet> tweets = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Retweet> retweets = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Message> sentMessages = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private Set<Message> receivedMessages = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            schema = "twitter",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addTweet(Tweet tweet){

        if( !tweets.contains(tweet)){
            this.tweets.add(tweet);
        }

    }

    public void deleteTweet(Tweet tweet){

        this.tweets.remove(tweet);
    }

    public void addComment(Comment comment){

        if( !comments.contains(comment)){
            this.comments.add(comment);
        }

    }

    public void deleteComment(Comment comment){

        this.comments.remove(comment);
    }

    public void addLike(Like like){

        if( !likes.contains(like)){
            this.likes.add(like);
        }

    }

    public void deleteLike(Like like){

        this.likes.remove(like);
    }

    public void addRetweet(Retweet retweet){

        if( !retweets.contains(retweet)){
            this.retweets.add(retweet);
        }

    }

    public void deleteRetweet(Retweet retweet){

        this.retweets.remove(retweet);
    }

    public void addBookmark(Bookmark bookmark){

        if( !bookmarks.contains(bookmark)){
            this.bookmarks.add(bookmark);
        }

    }

    public void deleteBookmark(Bookmark bookmark){

        this.bookmarks.remove(bookmark);
    }


    public void addFollowing(Follow following){

        if( !followings.contains(following)){
            this.followings.add(following);
        }

    }

    public void deleteFollowing(Follow following){

        this.followings.remove(following);
    }


    public void addFollower(Follow follower){

        if( !followers.contains(follower)){
            this.followers.add(follower);
        }

    }

    public void deleteFollower(Follow follower){

        this.followers.remove(follower);
    }

    public void addSentMessage(Message sentMessage){

        if( !sentMessages.contains(sentMessage)){
            this.sentMessages.add(sentMessage);
        }

    }

    public void deleteSentMessage(Message sentMessage){

        this.sentMessages.remove(sentMessage);
    }

    public void addReceivedMessage(Message receivedMessage){

        if( !receivedMessages.contains(receivedMessage)){
            this.receivedMessages.add(receivedMessage);
        }

    }

    public void deleteReceivedMessage(Message receivedMessage){

        this.receivedMessages.remove(receivedMessage);
    }
}

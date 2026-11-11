package com.myproject.twitter.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "messages", schema = "twitter")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"sender", "receiver"})
@EqualsAndHashCode( of = "id")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mesaj içeriği boş bırakılamaz.")
    @Size(max = 255)
    private String content;


    @NotNull(message = "Okunma durumu boş bırakılamaz.")
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Mesajı gönderen kullanıcı alanı boş bırakılamaz")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender ;

    @NotNull(message = "Mesajı alan kullanıcı alanı boş bırakılamaz")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

}

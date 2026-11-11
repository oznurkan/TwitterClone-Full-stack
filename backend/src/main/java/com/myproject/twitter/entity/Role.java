package com.myproject.twitter.entity;

import com.myproject.twitter.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "roles", schema = "twitter")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", unique = true, nullable = false, length = 50)
    private RoleType authority;


}

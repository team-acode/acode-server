package server.acode.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.common.BaseTimeEntity;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Where(clause = "is_del = false")
@Table(indexes = @Index(name = "idx_user_authkey", columnList = "auth_key"))
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50) default 'ROLE_USER'")
    private Role role;

    @Column(nullable = false)
    private String authKey;

    @Column(length = 30)
    private String nickname;

    @Column(columnDefinition = "integer default 0")
    private int reviewCnt;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isDel;

    @Builder
    public User(Long id, Role role, String authKey, String nickname){
        this.id = id;
        this.role = Objects.requireNonNullElse(role,Role.ROLE_USER);
        this.authKey = authKey;
        this.nickname = nickname;
        this.reviewCnt = 0;
        this.isDel = false;
    }

    public void setNickname(String newOne) {
        this.nickname = newOne;
    }

    public void setIsDel(boolean delColumn){
        this.isDel = delColumn;
    }
}

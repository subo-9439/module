package com.project.userModule.user.entity;

import com.project.userModule.user.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member")
public class User extends BaseEntity {
    // Entity 클래스는 매개변수가 없는 생성자의 접근 레벨이 public 또는 protected로 해야 한다.
    // 인스턴스 변수는 직접 접근이 아닌 내부 메소드로 접근해야 한다. -> setter사용
    @Builder
    public User(Long id, String email, String password, String name, String phone, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.nickname = nickname;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;
    private Status status;
    private Role role; // User, MANAGER, ADMIN

    public enum Status{
        INACTIVE("비활성화"),ACTIVE("활동화"), DELETE("삭제된유저");

        @Getter
        private String content;

        Status(String content){
            this.content = content;
        }
    }

    public enum Role{
        ROLE_USER("유저"), ROLE_ADMIN("관리자");


        @Getter
        private String role;

        Role(String role){
                this.role = role;
        }
    }
}

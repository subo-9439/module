package com.project.userModule.user.entity;


import com.project.userModule.user.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Users extends BaseEntity {
    // Entity 클래스는 매개변수가 없는 생성자의 접근 레벨이 public 또는 protected로 해야 한다.
    // 인스턴스 변수는 직접 접근이 아닌 내부 메소드로 접근해야 한다. -> setter사용
    @Builder
    public Users(Long id, String email, String password, String name, String phone, String nickname) {
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

    public enum Status{
        INACTIVE("비활성화"),ACTIVE("활동화"), DELETE("삭제된유저");

        @Getter
        private String content;

        Status(String content){
            this.content = content;
        }
    }
}

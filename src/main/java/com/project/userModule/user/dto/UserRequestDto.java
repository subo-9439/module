package com.project.userModule.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserRequestDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostJoin{

        @Builder
        public PostJoin(String email, String password, String name, String phone, String nickname) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.phone = phone;
            this.nickname = nickname;
        }

        private String email;
        private String password;
        private String name;
        private String phone;
        private String nickname;
    }
}

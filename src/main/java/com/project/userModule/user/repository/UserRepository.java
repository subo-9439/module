package com.project.userModule.user.repository;


import com.project.userModule.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByNickname(String nickname);
}

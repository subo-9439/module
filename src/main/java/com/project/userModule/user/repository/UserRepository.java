package com.project.userModule.user.repository;

import com.project.userModule.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);
}

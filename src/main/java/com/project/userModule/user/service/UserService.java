package com.project.userModule.user.service;

import com.project.userModule.user.entity.Users;
import com.project.userModule.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users joinMember(Users user){
        //1.이메일 인증 필요
        authenticateByEmail();
        //2. 핸드폰 인증 필요
        authenticateByPhone();

        return userRepository.findByEmail(user.getEmail());
    }
    public void authenticateByEmail(){

    }

    public void authenticateByPhone(){

    }
}

package com.example.ch16.service.Impl;

import com.example.ch16.config.security.JwtTokenProvider;
import com.example.ch16.dto.CommonResponse;
import com.example.ch16.dto.SignInResultDTO;
import com.example.ch16.dto.SignUpResultDTO;
import com.example.ch16.entity.User;
import com.example.ch16.repository.UserRepository;
import com.example.ch16.service.SignService;
import com.example.ch16.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {
    public UserRepository userRepository;
    public JwtTokenProvider jwtTokenProvider;
    public PasswordEncoder passwordEncoder;
    @Autowired
    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public SignUpResultDTO signUp(String id, String password, String name, String email, String role) {
        System.out.println("[SignUp] 회원가입");

        User user;

        if(role.equalsIgnoreCase("admin")) {
            user = new User(id, password, name, email, Collections.singletonList("ROLE_ADMIN"));
            //            user = User
//                    .builder()
//                    .uid(id).name(name)
//                    .password(passwordEncoder.encode(password))
//                    .roles(Collections.singletonList("ROLE_ADMIN")).build();
        } else {
            user = new User(id, password, name, email, Collections.singletonList("ROLE_USER"));
//            user = User
//                    .builder()
//                    .uid(id).name(name)
//                    .password(passwordEncoder.encode(password))
//                    .roles(Collections.singletonList("ROLE_USER")).build();
        }

        User savedUser = userRepository.save(user);
        SignUpResultDTO signUpResultDto = new SignInResultDTO();

        if(!savedUser.getName().isEmpty()) {
            setSuccessResult(signUpResultDto);
        } else {
            setFailResult(signUpResultDto);
        }
        return signUpResultDto;
    }
    private void setSuccessResult(SignUpResultDTO result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
    private void setFailResult(SignUpResultDTO result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

    @Override
    public SignInResultDTO signIn(String id, String password) throws RuntimeException {
        User user = userRepository.getByUid(id);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }
        SignInResultDTO signInResultDTO = SignInResultDTO.builder().
            token(jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRoles())).build();
        setSuccessResult(signInResultDTO);
        return signInResultDTO;
    }
}

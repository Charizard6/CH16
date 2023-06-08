package com.example.ch16.service;

import com.example.ch16.dto.SignInResultDTO;
import com.example.ch16.dto.SignUpResultDTO;

public interface SignService {
    SignUpResultDTO signUp(String id, String password, String name, String email, String role);
    SignInResultDTO signIn(String id, String password) throws RuntimeException;
}

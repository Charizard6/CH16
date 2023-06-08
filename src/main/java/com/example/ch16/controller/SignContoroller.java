package com.example.ch16.controller;

import com.example.ch16.dto.SignInResultDTO;
import com.example.ch16.dto.SignUpResultDTO;
import com.example.ch16.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign-api")
public class SignContoroller {
    private final SignService signService;
    @Autowired
    public SignContoroller(SignService signService) {
        this.signService = signService;
    }
    @PostMapping("/sign-in")
    public SignInResultDTO signIn(@RequestParam String id, @RequestParam String password)
        throws RuntimeException {
        SignInResultDTO signInResultDTO = signService.signIn(id, password);
        if(signInResultDTO.getCode() == 0) {
            System.out.println("정상 로그인");
        }
        return signInResultDTO;
    }
    @PostMapping("/sign-up")
    public SignUpResultDTO signUp(@RequestParam String id, @RequestParam String password,
        @RequestParam String name, @RequestParam String email, @RequestParam String role)
        throws RuntimeException {
        SignUpResultDTO signUpResultDTO = signService.signUp(id, password, name, email, role);
        return signUpResultDTO;
    }
    @GetMapping("/exception")
    public void exception() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었다");
    }
}

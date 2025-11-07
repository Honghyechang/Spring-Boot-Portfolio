package com.example.Spring.Board.Project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordForm {
    @NotBlank(message = "기존의 패스워드를 입력해주세요.")
    private String old;
    @NotBlank(message="새로운 패스워드를 입력해주세요.")
    @Size(min = 8 , message = "8글자 이상 입력해주세요.")
    private String password;
    @NotBlank(message = "새로운 패스워드를 확인해주세요")
    private String passwordConfirm;
}

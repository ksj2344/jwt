package com.green.jwt.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title="로그인")
public class UserSignInReq {
    @Schema(title="ID", example = "dddd3@naver.com", requiredMode=Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(title="PW", example = "1212", requiredMode=Schema.RequiredMode.REQUIRED)
    private String pw;
}

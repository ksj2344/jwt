package com.green.jwt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "JWT",
                version = "v0.1",
                description = "JWT Practice"
        )
        , security = @SecurityRequirement(name="Authorization")
)

@SecurityScheme(
          type= SecuritySchemeType.HTTP
        , name="Authorization"  //yml의 const:jwt: header-schema-name와 같음
        , in= SecuritySchemeIn.HEADER
        , bearerFormat = "JWT"
        , scheme = "Bearer"
)
public class SwaggerConfiguration {}

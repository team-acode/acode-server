package server.acode.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

//    @Bean
//    public OpenAPI publicApi() {
//        Server server = new Server().url("/");
//
//        return new OpenAPI()
//                .info(getSwaggerInfo())
//                .components(authSetting())
//                .addServersItem(server);
//    }
//
//    private Info getSwaggerInfo() {
//        License license = new License();
//        license.setName("Acode");
//
//        return new Info()
//                .title("Acode API 명세서")
//                .description("Acode API 명세서입니다")
//                .version("v0.0.1")
//                .license(license);
//    }
//
//    private Components authSetting() {
//        return new Components()
//                .addSecuritySchemes(
//                        "Authorization",
//                        new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                                .in(SecurityScheme.In.HEADER)
//                                .name("Authorization"));
//    }
    @Bean
    public OpenAPI openAPI() {
    Info info = new Info()
            .version("v1.0.0")
            .title("어코드 API")
            .description("어코드 API입니다");

    String jwt = "JWT";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt); // 헤더에 토큰 포함
    Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
            .name(jwt)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
    );

    return new OpenAPI()
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components);
}


}

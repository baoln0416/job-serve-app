//package com.jobserve.gateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AppConfig {
//
//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/companies/**")
//                        .uri("http://localhost:8081/"))
//                .route(r -> r.path("/jobs/**")
//                        .uri("http://localhost:8082/"))
//                .route(r -> r.path("/reviews/**")
//                        .uri("http://localhost:8083/"))
//                .build();
//    }
//}

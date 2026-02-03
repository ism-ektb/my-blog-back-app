package ru.ism.myblogbackapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.ism.myblogbackapp.controller", "ru.ism.myblogbackapp.exception"})
public class WebConfig {
}

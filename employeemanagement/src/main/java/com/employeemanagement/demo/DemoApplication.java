package com.employeemanagement.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Employee Management System", version = "1.0", description = "An application to maintain employee records. Employees are also users of the application. They can login using their emailId and password. Authorities are granted using roles and privileges."))
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.APIKEY, scheme = "Bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER, description = "Enter Bearer token obtained after authentication at '/login' endpoint")
@ComponentScan(basePackages = "com.employeemanagement.demo")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
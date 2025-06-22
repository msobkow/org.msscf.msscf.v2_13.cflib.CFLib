package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

@SpringBootApplication
public class CFDbTestApplication {
    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");
        File userPropsFile = new File(userHome, ".cfdbtest.properties");
        File defaultPropsFile = new File("src/main/resources/application.properties");

        if (!userPropsFile.exists()) {
            try {
                Files.copy(defaultPropsFile.toPath(), userPropsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("A new user properties file has been created at: " + userPropsFile.getAbsolutePath());
                System.out.println("Please customize this file before running the application again.");
                System.exit(0);
            } catch (IOException e) {
                System.err.println("Failed to create user properties file: " + e.getMessage());
                System.exit(1);
            }
        }

        SpringApplicationBuilder builder = new SpringApplicationBuilder(CFDbTestApplication.class);
        builder.properties("spring.config.location=" + userPropsFile.getAbsolutePath());
        builder.run(args);
    }
}

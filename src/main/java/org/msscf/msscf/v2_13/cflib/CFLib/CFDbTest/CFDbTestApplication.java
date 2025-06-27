package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class,
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class
})
@EntityScan(basePackages = "org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest.appdb")
public class CFDbTestApplication {
    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");
        File userPropsFile = new File(userHome, ".cfdbtest.properties");

        // Load default properties from the compiled-in resource
        if (!userPropsFile.exists()) {
            try (var in = CFDbTestApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (in == null) {
                    throw new IOException("Default application.properties resource not found in classpath");
                }
                Files.copy(in, userPropsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("A new user properties file has been created at: " + userPropsFile.getAbsolutePath());
                System.out.println("Please customize this file before running the application again.");
                System.exit(0);
            } catch (IOException e) {
                System.err.println("Failed to create user properties file: " + e.getMessage());
                System.exit(1);
            }
        }

        SpringApplication.run(CFDbTestApplication.class, args);
    }
}

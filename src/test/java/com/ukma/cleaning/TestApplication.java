package com.ukma.cleaning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.Statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.ukma.cleaning.user.UserService;
import com.ukma.cleaning.user.dto.UserRegistrationDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
//@PropertySource("classpath:com/ukma/cleaning/resources/application.properties")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleaningApplication.class, args);
	}

    public static void setupUsers(){
        setup("src\\test\\resources\\testUsers.sql");
    }

    public static void setupAll() {
        setup("src\\test\\resources\\testData.sql");    
    }

    private static void setup(String path){
        try {
            var connection = DriverManager.getConnection("jdbc:h2:mem:cleaning", "sa", "sa");
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET SCHEMA cleaning");
            } catch (Exception e) {
                log.warn("An exception occurred:", e);
            }

            InputStream inputStream = new FileInputStream(new File(path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            if (inputStream != null) {
                String line;
                Statement statement = connection.createStatement();
                while ((line = reader.readLine()) != null) {
                    if (!line.isEmpty()) {
                        statement.execute(line);
                    }
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}


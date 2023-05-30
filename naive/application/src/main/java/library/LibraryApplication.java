package library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor 
public class LibraryApplication {

    public static final String UI_CONFIG_NAME = "uiConfig";

    public static void main(String[] args){
        SpringApplication.run(LibraryApplication.class, args);
    }
}
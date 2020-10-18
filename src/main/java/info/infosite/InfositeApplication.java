package info.infosite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class InfositeApplication {

	public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(InfositeApplication.class, args);
    }
}


package info.infosite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"info.infosite"})
public class InfositeApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfositeApplication.class, args);
	}

}

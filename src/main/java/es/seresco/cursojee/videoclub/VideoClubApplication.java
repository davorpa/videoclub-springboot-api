package es.seresco.cursojee.videoclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="es.seresco.cursojee")
public class VideoClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoClubApplication.class, args);
	}

}

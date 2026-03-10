package ma.jobintech.projetfilrouge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Equipe1BackendSprinApplication {
    public static void main(String[] args) {
        SpringApplication.run(Equipe1BackendSprinApplication.class, args);
    }
}
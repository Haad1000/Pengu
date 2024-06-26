package code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
@EnableJpaRepositories
@EnableWebSocket
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args) ;
    }


}

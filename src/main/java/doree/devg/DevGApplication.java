package doree.devg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFilter;

@SpringBootApplication
public class DevGApplication {


	public static void main(String[] args) {
		SpringApplication.run(DevGApplication.class, args);
	}

}

package cp.chargeotg.authorization;

import org.springframework.boot.SpringApplication;

public class TestAuthzServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(AuthzServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

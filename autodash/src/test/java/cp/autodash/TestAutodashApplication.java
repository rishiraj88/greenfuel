package cp.autodash;

import org.springframework.boot.SpringApplication;

public class TestAutodashApplication {

	public static void main(String[] args) {
		SpringApplication.from(AutodashApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

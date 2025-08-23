package nl.han.devops.definitions;

import nl.han.devops.PriemTesterApplication;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = PriemTesterApplication.class)
public class CucumberSpringConfiguration {
}

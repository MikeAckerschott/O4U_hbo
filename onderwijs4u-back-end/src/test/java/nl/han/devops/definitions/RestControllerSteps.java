package nl.han.devops.definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.annotation.PostConstruct;
import nl.han.devops.PriemTesterApplication;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = PriemTesterApplication.class)
@ActiveProfiles("test")
public class RestControllerSteps {

    @LocalServerPort
    private int port;

    private Response response;

    @Given("the REST controller is up and running")
    public void theRestControllerIsUpAndRunning() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        System.out.println("Setting up RestAssured with port: " + port);

        System.out.println("Server running on port: " + port);

        Response healthCheckResponse = given().when().get("/health");
        if (healthCheckResponse.getStatusCode() != 200) {
            throw new RuntimeException("REST controller is not up and running");
        }
    }

    @When("I make a GET request to {string}")
    public void iMakeAGETRequestTo(String uri) {
        int attempts = 0;
        while (attempts < 10) {
            try {
                response = given().when().get(uri);
                return;
            } catch (Exception e) {
                attempts++;
                System.out.println("Attempt " + attempts + " failed, retrying...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        throw new RuntimeException("Failed to connect after 5 attempts");
    }


    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("the response body should be {string}")
    public void theResponseBodyShouldBe(String expectedBody) {
        response.then().body(equalTo(expectedBody));
    }
}

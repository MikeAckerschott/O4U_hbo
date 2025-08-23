Feature: Prime Number Verification
  Scenario: Get request to the /priem/{number} endpoint should return true
    Given the REST controller is up and running
    When I make a GET request to "/priem/5"
    Then the response status should be 200
    And the response body should be "true"
  Scenario: Get request to the /priem/{number} endpoint should return false
    Given the REST controller is up and running
    When I make a GET request to "/priem/4"
    Then the response status should be 200
    And the response body should be "false"
Feature: The application's GET User Data endpoint

  Scenario: The backend is able to process User Data with userId GET requests
    Given LAU EUD backend application is healthy
    When And I GET "/audit/userData" using query param either userId "13e31622-edea-493c-8240-9b780c9d6111" or email ""
    Then a single userData response body is returned for for param userId "13e31622-edea-493c-8240-9b780c9d6111" or email ""


  Scenario: The backend is able to process User Data with emailId GET requests
    Given LAU EUD backend application is healthy
    When And I GET "/audit/userData" using query param either userId "" or email "john111.smith111@example.org"
    Then a single userData response body is returned for for param userId "" or email "john111.smith111@example.org"

  Scenario: The backend is unable to process userData GET requests due to missing s2s
    Given LAU EUD backend application is healthy
    When And I GET "/audit/userData" without service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process userData GET requests due to missing mandatory params
    Given LAU EUD backend application is healthy
    When I request GET "/audit/userData" endpoint without mandatory params
    Then HTTP "400" Bad Request response is returned

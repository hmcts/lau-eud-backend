Feature: The application's GET User Updates endpoint

  @userUpdates
  Scenario: The backend is able to process User Updates with userId GET requests
    Given LAU EUD backend application is healthy
    And I GET "/audit/userUpdates" using query param userId "13e31622-edea-493c-8240-9b780c9d6111" with page "0" and size "10"
    And a userUpdates response body is returned for userId "13e31622-edea-493c-8240-9b780c9d6111"
    And userUpdates totalElements is 7
    And database has 7 user update records
    And userUpdates contains change "email" with value "new.email@example.org" and previous "old.email@example.org"
    And userUpdates contains change "forename" with value "Jane" and previous "John"
    And userUpdates contains change "surname" with value "Doe" and previous "Smith"
    And userUpdates contains change "displayName" with value "Jane Doe" and previous "John Smith"
    And userUpdates contains change "roleNames" with value "caseworker,citizen" and previous "citizen"
    And userUpdates contains change "accountStatus" with value "SUSPENDED" and previous "ACTIVE"
    And userUpdates contains change "recordType" with value "ARCHIVED" and previous "LIVE"

  Scenario: The backend is unable to process User Updates GET requests due to missing s2s
    Given LAU EUD backend application is healthy
    When And I GET "/audit/userUpdates" without service authorization header
    Then HTTP "400" Bad Request response is returned

  Scenario: The backend is unable to process User Updates GET requests due to missing mandatory params
    Given LAU EUD backend application is healthy
    When I request GET "/audit/userUpdates" endpoint without mandatory params
    Then HTTP "400" Bad Request response is returned

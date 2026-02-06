Feature: The application's GET User Updates endpoint

  @userUpdates
  Scenario: The backend is able to process User Updates with userId GET requests
    Given LAU EUD backend application is healthy
    And I GET "/audit/userUpdates" using query param userId "13e31622-edea-493c-8240-9b780c9d6111" with page "0" and size "10"
    And a userUpdates response body is returned for userId "13e31622-edea-493c-8240-9b780c9d6111"
    And userUpdates totalElements is 7
    And database has 7 user update records
    And userUpdates contains changes:
      | fieldName     | value                 | previousValue         |
      | email         | new.email@example.org | old.email@example.org |
      | forename      | Jane                  | John                  |
      | surname       | Doe                   | Smith                 |
      | displayName   | Jane Doe              | John Smith            |
      | roleNames     | caseworker,citizen    | citizen               |
      | accountStatus | SUSPENDED             | ACTIVE                |
      | recordType    | ARCHIVED              | LIVE                  |


  Scenario: The backend is unable to process User Updates GET requests due to missing s2s
    Given LAU EUD backend application is healthy
    When I GET "/audit/userUpdates" without service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process User Updates GET requests due to invalid s2s
    Given LAU EUD backend application is healthy
    When I GET "/audit/userUpdates" using query param userId "13e31622-edea-493c-8240-9b780c9d6111" with invalid service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process User Updates GET requests due to missing mandatory params
    Given LAU EUD backend application is healthy
    When I request GET "/audit/userUpdates" endpoint without mandatory params
    Then HTTP "400" Bad Request response is returned

Feature: The application's GET User Updates endpoint

  @userUpdates
  Scenario: The backend is able to process User Updates with userId GET requests
    Given LAU EUD backend application is healthy
    And I GET "/audit/userUpdates" using query param userId "13e31622-edea-493c-8240-9b780c9d6111" with page "0" and size "10"
    And a userUpdates response body is returned for userId "13e31622-edea-493c-8240-9b780c9d6111"
    And userUpdates totalElements is 7
    And database has 7 user update records
    And userUpdates previous values are:
      | email                 | forename | surname | displayName | roleNames          | accountStatus  | recordType |
      | old.email@example.org | John     | Smith   | John Smith  | citizen            | ACTIVE         | LIVE       |
    And userUpdates changed values are:
      | email                 | forename | surname | displayName | roleNames          | accountStatus | recordType |
      | new.email@example.org | Jane     | Doe     | Jane Doe    | caseworker,citizen | SUSPENDED     | ARCHIVED   |


  Scenario: The backend is unable to process User Updates GET requests due to missing s2s
    Given LAU EUD backend application is healthy
    When And I GET "/audit/userUpdates" without service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process User Updates GET requests due to invalid s2s
    Given LAU EUD backend application is healthy
    When And I GET "/audit/userUpdates" using query param userId "13e31622-edea-493c-8240-9b780c9d6111" with invalid service authorization header
    Then HTTP "403" Forbidden response is returned

  Scenario: The backend is unable to process User Updates GET requests due to missing mandatory params
    Given LAU EUD backend application is healthy
    When I request GET "/audit/userUpdates" endpoint without mandatory params
    Then HTTP "400" Bad Request response is returned

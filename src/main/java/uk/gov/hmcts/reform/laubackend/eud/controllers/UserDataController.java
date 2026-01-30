package uk.gov.hmcts.reform.laubackend.eud.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataValidation;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserUpdate;
import uk.gov.hmcts.reform.laubackend.eud.response.UserDataResponse;
import uk.gov.hmcts.reform.laubackend.eud.response.UserUpdatesResponse;
import uk.gov.hmcts.reform.laubackend.eud.service.UserDataService;
import uk.gov.hmcts.reform.laubackend.eud.service.UserUpdatesService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.eud.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "User Details Operation", description = "This is the Log and audit for Enhanced User Deatils"
    + "Back-End API that will provide user data. "
    + "The API will be invoked by LAU front-end service (GET).")
public class UserDataController {

    private final UserDataService userDataService;
    private final UserUpdatesService userUpdatesService;

    @Operation(
        tags = "User Data endpoints",
        summary = "Retrieve User Data",
        description = "Query User Data based on search conditions provided via URL params"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Request executed successfully. Response will contain details of user",
        content = { @Content(schema = @Schema(implementation = UserDataResponse.class))})
    @ApiResponse(responseCode = "400", description = "Missing required parameter. userId or emailAddress is mandatory")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping(path = "/audit/userData", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDataResponse> getUserData(
        @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String authToken,
        @Validated(UserDataValidation.EitherUserIdOrEmailRequired.class) @Valid UserDataGetRequestParams requestParams
    ) {
        try {
            final UserDataResponse response = userDataService.getUserData(requestParams);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (final Exception ex) {
            log.error("getUserData API call failed due to error - {}", ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
        tags = "User Data endpoints",
        summary = "Retrieve User Account updates",
        description = "Query User Account updates for a given user id"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Request executed successfully. Response will contain user account updates",
        content = { @Content(schema = @Schema(implementation = UserUpdatesResponse.class))})
    @ApiResponse(responseCode = "400", description = "userId is mandatory")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping(path = "/audit/userUpdates", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserUpdatesResponse> getUserAccountUpdates(
        @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String authToken,
        @Validated(UserDataValidation.UserIdRequired.class) @Valid UserDataGetRequestParams requestParams,
        @ParameterObject Pageable pageable
    ) {
        try {
            Page<UserUpdate> page = userUpdatesService.getUserUpdates(requestParams.getUserId(), pageable);
            return ResponseEntity.ok(UserUpdatesResponse.from(page));
        } catch (Exception ex) {
            log.error("GetUserAccountUpdates API call failed due to error - {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError().build();
        }
    }

}

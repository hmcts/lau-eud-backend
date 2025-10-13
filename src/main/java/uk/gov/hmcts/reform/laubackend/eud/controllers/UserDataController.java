package uk.gov.hmcts.reform.laubackend.eud.controllers;

import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.response.UserDataResponse;
import uk.gov.hmcts.reform.laubackend.eud.service.UserDataService;

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

    @Operation(
        tags = "User Data endpoints",
        summary = "Retrieve User Data",
        description = "Query User Data based on search conditions provided via URL params"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Request executed successfully. Response will contain details of user",
        content = { @Content(schema = @Schema(implementation = UserDataResponse.class))})
    @ApiResponse(
        responseCode = "400",
        description = "Missing required parameter. userId or emailAddress is mandatory")
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden")
    @ApiResponse(
        responseCode = "404",
        description = "Not Found")
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error")
    @GetMapping(
        path = "/audit/userData",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDataResponse> getUserData(
        @Parameter(name = "Service Authorization", example = "Bearer eyJ0eXAiOiJK.........")
        @RequestHeader(value = SERVICE_AUTHORISATION_HEADER) String authToken,
        @Valid UserDataGetRequestParams requestParams
    ) {
        try {

            final UserDataResponse response = userDataService.getUserData(requestParams);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (FeignException.NotFound e) {
            log.error("getUserData API call failed due to error - {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (final Exception ex) {
            log.error("getUserData API call failed due to error - {}", ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

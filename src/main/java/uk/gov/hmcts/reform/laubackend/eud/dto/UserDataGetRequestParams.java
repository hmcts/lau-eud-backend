package uk.gov.hmcts.reform.laubackend.eud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestParam;

@Schema(description = "Data model for the User Data request")
public record UserDataGetRequestParams(
    @RequestParam String userId,
    @RequestParam String email) {
}

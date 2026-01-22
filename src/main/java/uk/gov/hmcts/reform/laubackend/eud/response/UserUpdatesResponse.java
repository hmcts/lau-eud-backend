package uk.gov.hmcts.reform.laubackend.eud.response;

import org.springframework.data.domain.Page;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserUpdate;

import java.util.List;

public record UserUpdatesResponse(
    List<UserUpdate> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public static UserUpdatesResponse from(Page<UserUpdate> page) {
        return new UserUpdatesResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}

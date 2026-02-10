package uk.gov.hmcts.reform.laubackend.eud.functionaltests.model;

import java.util.List;

public record UserUpdatesResponse(
    List<UserUpdate> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {}

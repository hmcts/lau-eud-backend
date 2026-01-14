package uk.gov.hmcts.reform.laubackend.eud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.dto.IdamEvent;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceBusMessageHandler {

    private final UserDiffService userDiffService;
    private final IdamUserChangeAuditRepository repository;

    public void handleMessage(IdamEvent idamEvent) {
        List<UserDiffService.FieldChange> changes = userDiffService.diffUsers(
            idamEvent.previousUser(), idamEvent.user());
        List<IdamUserChangeAudit> entities = new ArrayList<>();
        String userId = idamEvent.user().id();
        for (UserDiffService.FieldChange change : changes) {
            entities.add(IdamUserChangeAudit.builder()
                .userId(userId)
                .principalUserId(idamEvent.principalId())
                .eventType(idamEvent.eventType())
                .eventName(change.fieldName())
                .eventValue(change.currentValue())
                .previousEventValue(change.previousValue())
                .eventTimestamp(idamEvent.eventDateTimeUtc())
                .build());
        }
        repository.saveAll(entities);
    }
}

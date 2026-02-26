package uk.gov.hmcts.reform.laubackend.eud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.dto.IdamEvent;
import uk.gov.hmcts.reform.laubackend.eud.dto.User;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditCustomRepository;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceBusMessageHandler {

    private final UserDiffService userDiffService;
    private final IdamUserChangeAuditCustomRepository customRepository;
    private final IdamUserChangeAuditRepository repository;

    @Value("${lau.db.encryption-key}")
    private String encryptionKey;
    @Value("${lau.db.encryption-enabled}")
    private Boolean encryptionEnabled;

    public void handleMessage(IdamEvent idamEvent) {
        if (idamEvent.eventType() == EventType.MODIFY && idamEvent.previousUser() == null) {
            log.info(
                "Skipping modify event as it has no previous user. Principal user id {}, user id {}",
                idamEvent.principalId(),
                idamEvent.user().id());
            return;
        }
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

        if (BooleanUtils.isTrue(encryptionEnabled)) {
            customRepository.saveAllEncrypted(entities, encryptionKey);
        } else {
            repository.saveAll(entities);
        }
    }

    public void handleAddMessage(IdamEvent idamEvent) {
        User idamUser =  idamEvent.user();
        if (idamUser == null) {
            log.error("Received EMPTY user for ADD event @{}", idamEvent.eventDateTimeUtc());
            return;
        }
        IdamUserChangeAudit audit = IdamUserChangeAudit.builder()
            .userId(idamUser.id())
            .eventType(idamEvent.eventType())
            .eventName("Create user")
            .eventValue("User Created")
            .eventTimestamp(idamEvent.eventDateTimeUtc())
            .build();
        if (StringUtils.isNotBlank(idamEvent.principalId())) {
            audit.setPrincipalUserId(idamEvent.principalId());
        }
        if (BooleanUtils.isTrue(encryptionEnabled)) {
            customRepository.saveEncrypted(audit, encryptionKey);
        } else {
            repository.save(audit);
        }
    }
}

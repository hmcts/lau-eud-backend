package uk.gov.hmcts.reform.laubackend.eud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserUpdate;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditCustomRepository;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;

@Service
@RequiredArgsConstructor
public class UserUpdatesService {
    private final IdamUserChangeAuditCustomRepository customRepository;
    private final IdamUserChangeAuditRepository repository;

    @Value("${lau.db.encryption-key}")
    private String encryptionKey;

    @Value("${lau.db.encryption-enabled}")
    private Boolean encryptionEnabled;

    public Page<UserUpdate> getUserUpdates(String userId, Pageable pageable) {
        if (Boolean.TRUE.equals(encryptionEnabled)) {
            return customRepository.findIdamUserChangeAuditsByUserId(userId, pageable, encryptionKey);
        } else {
            Page<IdamUserChangeAudit> page = repository.findIdamUserChangeAuditsByUserId(userId, pageable);
            return page.map(UserUpdate::from);
        }
    }
}

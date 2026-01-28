package uk.gov.hmcts.reform.laubackend.eud.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;

@Repository
public interface IdamUserChangeAuditRepository extends JpaRepository<IdamUserChangeAudit, Long> {
    Page<IdamUserChangeAudit> findIdamUserChangeAuditsByUserId(String userId, Pageable pageable);
}

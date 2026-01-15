package uk.gov.hmcts.reform.laubackend.eud.repository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class IdamUserChangeAuditCustomRepository {
    private static final String INSERT_SQL = """
        INSERT INTO public.idam_user_change_audit
            (user_id, principal_user_id, event_type, event_name,
             event_value, previous_event_value, event_timestamp)
        VALUES (
            :userId,
            :principalUserId,
            :eventType,
            :eventName,
            encode(pgp_sym_encrypt(cast(:eventValue as text), cast(:encryptionKey as text)), 'base64'),
            encode(pgp_sym_encrypt(cast(:previousEventValue as text), cast(:encryptionKey as text)), 'base64'),
            :eventTimestamp
        )
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllEncrypted(List<IdamUserChangeAudit> entities, String encryptionKey) {
        if (entities.isEmpty()) {
            return;
        }

        SqlParameterSource[] batch = entities.stream()
            .map(e -> new MapSqlParameterSource()
                .addValue("userId", e.getUserId())
                .addValue("principalUserId", e.getPrincipalUserId())
                .addValue("eventType", e.getEventType().name())
                .addValue("eventName", e.getEventName())
                .addValue("eventValue", e.getEventValue())
                .addValue("previousEventValue", e.getPreviousEventValue())
                .addValue("eventTimestamp", e.getEventTimestamp())
                .addValue("encryptionKey", encryptionKey)
            )
            .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(INSERT_SQL, batch);
    }
}

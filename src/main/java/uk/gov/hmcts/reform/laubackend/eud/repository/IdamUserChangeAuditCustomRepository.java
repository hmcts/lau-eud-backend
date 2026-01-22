package uk.gov.hmcts.reform.laubackend.eud.repository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserUpdate;

import java.time.OffsetDateTime;
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

    private static final String COUNT_SQL = """
            SELECT count(*)
            FROM idam_user_change_audit
            WHERE user_id = :userId
            """;

    private static final String SELECT_SQL = """
        SELECT
            event_name,
            event_type,
            pgp_sym_decrypt(decode(event_value, 'base64'), cast(:encryptionKey as text))::text AS event_value,
            pgp_sym_decrypt(
                decode(previous_event_value, 'base64'),
                cast(:encryptionKey as text))::text AS previous_event_value,
            event_timestamp,
            principal_user_id
        FROM idam_user_change_audit
        WHERE user_id = :userId
        ORDER BY event_timestamp ASC
        LIMIT :limit OFFSET :offset
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

    public Page<UserUpdate> findIdamUserChangeAuditsByUserId(String userId, Pageable pageable, String encryptionKey) {
        Long total = jdbcTemplate.queryForObject(COUNT_SQL, new MapSqlParameterSource("userId", userId), Long.class);
        if (total == null || total == 0) {
            return Page.empty(pageable);
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("encryptionKey", encryptionKey)
            .addValue("limit", pageable.getPageSize())
            .addValue("offset", pageable.getOffset());
        List<UserUpdate> content = jdbcTemplate.query(SELECT_SQL, params, (rs, rowNum) -> new UserUpdate(
            rs.getString("event_name"),
            EventType.valueOf(rs.getString("event_type")),
            rs.getString("event_value"),
            rs.getObject("event_timestamp", OffsetDateTime.class),
            rs.getString("principal_user_id"),
            rs.getString("previous_event_value")
        ));
        return new PageImpl<>(content, pageable, total);
    }
}

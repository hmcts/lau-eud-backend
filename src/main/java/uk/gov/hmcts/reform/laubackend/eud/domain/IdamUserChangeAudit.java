package uk.gov.hmcts.reform.laubackend.eud.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity(name = "idam_user_change_audit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdamUserChangeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "principal_user_id", length = 64)
    private String principalUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 64)
    private EventType eventType;

    @Column(name = "event_name", nullable = false, length = 64)
    private String eventName;

    @Column(name = "event_value", nullable = false)
    private String eventValue;

    @Column(name = "previous_event_value")
    private String previousEventValue;

    @Column(name = "event_timestamp", nullable = false)
    private OffsetDateTime eventTimestamp;

}

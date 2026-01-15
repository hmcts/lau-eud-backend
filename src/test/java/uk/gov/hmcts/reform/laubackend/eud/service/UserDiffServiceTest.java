package uk.gov.hmcts.reform.laubackend.eud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.laubackend.eud.domain.AccountStatus;
import uk.gov.hmcts.reform.laubackend.eud.dto.User;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserDiffServiceTest {
    private UserDiffService service;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        service = new UserDiffService(mapper);
        ReflectionTestUtils.setField(service, "ignoreChangesInFields", Set.of());
    }

    @Test
    void shouldReturnNoChangesWhenUsersAreEqual() {
        User u1 = new UserTestData().build();
        User u2 = new UserTestData().build();

        List<UserDiffService.FieldChange> changes = service.diffUsers(u1, u2);

        assertThat(changes).isEmpty();
    }

    @Test
    void shouldDetectChangedScalarField() {
        User previous = new UserTestData().build();
        User current = new UserTestData().withAccountStatus(AccountStatus.SUSPENDED).build();

        List<UserDiffService.FieldChange> changes = service.diffUsers(previous, current);
        assertThat(changes).hasSize(1);
        UserDiffService.FieldChange change = changes.getFirst();

        assertThat(change.fieldName()).isEqualTo("accountStatus");
        assertThat(change.currentValue()).isEqualTo("SUSPENDED");
        assertThat(change.previousValue()).isEqualTo("ACTIVE");
    }

    @Test
    void shouldIgnoreConfiguredFields() {
        ReflectionTestUtils.setField(service, "ignoreChangesInFields", Set.of("lastModified", "createDate"));
        OffsetDateTime lastModified = Instant.parse("2026-01-10T12:26:25.096041892Z").atOffset(ZoneOffset.UTC);
        User previous = new UserTestData().build();
        User current = new UserTestData().withLastModified(lastModified).build();

        List<UserDiffService.FieldChange> changes = service.diffUsers(previous, current);
        assertThat(changes).isEmpty();
    }

    @Test
    void shouldTreatRoleNamesAsOrderIndependent() {
        User previous = new UserTestData().withRoleNames(List.of("b-role", "a-role")).build();
        User current = new UserTestData().withRoleNames(List.of("a-role", "b-role")).build();

        List<UserDiffService.FieldChange> changes = service.diffUsers(previous, current);
        assertThat(changes).isEmpty();
    }

    @Test
    void shouldDetectRoleNamesAddedOrRemoved() {
        User previous = new UserTestData().withRoleNames(List.of("user")).build();
        User current = new UserTestData().withRoleNames(List.of("user", "admin")).build();

        List<UserDiffService.FieldChange> changes = service.diffUsers(previous, current);
        assertThat(changes).hasSize(1);

        UserDiffService.FieldChange change = changes.getFirst();
        assertThat(change.previousValue()).isEqualTo("user");
        assertThat(change.currentValue()).isEqualTo("admin,user");
    }

    @Test
    void shouldHandleNullValues() {
        User previous = new UserTestData().withDisplayName(null).build();
        User current = new UserTestData().withDisplayName("John Smith").build();

        List<UserDiffService.FieldChange> changes = service.diffUsers(previous, current);
        assertThat(changes).hasSize(1);

        UserDiffService.FieldChange change = changes.getFirst();
        assertThat(change.previousValue()).isNull();
        assertThat(change.currentValue()).isEqualTo("John Smith");
    }

}

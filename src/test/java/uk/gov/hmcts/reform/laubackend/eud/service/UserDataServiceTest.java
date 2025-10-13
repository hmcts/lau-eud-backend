package uk.gov.hmcts.reform.laubackend.eud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.response.UserDataResponse;
import uk.gov.hmcts.reform.laubackend.eud.service.remote.client.IdamClient;
import uk.gov.hmcts.reform.laubackend.eud.utils.IdamTokenGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserDataServiceTest {

    @Mock
    private IdamClient idamClient;

    @Mock
    private IdamTokenGenerator idamTokenGenerator;

    @InjectMocks
    private UserDataService userDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserDataByUserId() {
        String userId = "12345";
        UserDataGetRequestParams params = mock(UserDataGetRequestParams.class);
        when(params.getUserId()).thenReturn(userId);
        when(params.getEmail()).thenReturn(null);

        UserDataResponse expectedResponse = new UserDataResponse();
        expectedResponse.setUserId(userId);
        expectedResponse.setEmail("test@test.com");
        expectedResponse.setRoles(new ArrayList<String>(Arrays.asList("role1", "role2")));
        expectedResponse.setAccountStatus("ACTIVE");

        String token = "mock-token";
        when(idamTokenGenerator.generateIdamToken()).thenReturn(token);
        when(idamClient.getUserDataByUserId(token, userId)).thenReturn(expectedResponse);

        UserDataResponse actualResponse = userDataService.getUserData(params);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(idamTokenGenerator, times(1)).generateIdamToken();
        verify(idamClient, times(1)).getUserDataByUserId(token, userId);
        verify(idamClient, never()).getUserDataByEmail(anyString(), anyString());
    }

    @Test
    void shouldReturnUserDataByEmail() {
        String email = "test@example.com";
        UserDataGetRequestParams params = mock(UserDataGetRequestParams.class);
        when(params.getUserId()).thenReturn(null);
        when(params.getEmail()).thenReturn(email);

        String token = "mock-token";
        UserDataResponse expectedResponse = new UserDataResponse();
        when(idamTokenGenerator.generateIdamToken()).thenReturn(token);
        when(idamClient.getUserDataByEmail(token, email)).thenReturn(expectedResponse);

        UserDataResponse actualResponse = userDataService.getUserData(params);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(idamTokenGenerator, times(1)).generateIdamToken();
        verify(idamClient, times(1)).getUserDataByEmail(token, email);
        verify(idamClient, never()).getUserDataByUserId(anyString(), anyString());
    }

    @Test
    void shouldHandleExceptionGracefully() {
        String userId = "12345";
        UserDataGetRequestParams params = mock(UserDataGetRequestParams.class);
        when(params.getUserId()).thenReturn(userId);
        when(params.getEmail()).thenReturn(null);

        String token = "mock-token";
        when(idamTokenGenerator.generateIdamToken()).thenReturn(token);
        when(idamClient.getUserDataByUserId(token, userId)).thenThrow(new RuntimeException("Service error"));

        RuntimeException exception =
            assertThrows(RuntimeException.class, () -> userDataService.getUserData(params));
        assertEquals("Service error", exception.getMessage());
        verify(idamTokenGenerator, times(1)).generateIdamToken();
        verify(idamClient, times(1)).getUserDataByUserId(token, userId);
    }
}

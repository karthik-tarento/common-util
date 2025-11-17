package org.igot.common;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiResponseTest {

    @Test
    @DisplayName("Should create ApiResponse with default values")
    void constructor_Default_SetsDefaultValues() {
        ApiResponse response = new ApiResponse();

        assertEquals("v1", response.getVer());
        assertNotNull(response.getTs());
        assertNotNull(response.getParams());
        assertNotNull(response.getParams().getResmsgid());
        assertNotNull(response.getParams().getMsgid());
        assertNotNull(response.getResponse());
        assertTrue(response.getResponse().isEmpty());
    }

    @Test
    @DisplayName("Should create ApiResponse with id")
    void constructor_WithId_SetsIdAndDefaults() {
        String apiId = "api.test.endpoint";
        ApiResponse response = new ApiResponse(apiId);

        assertEquals(apiId, response.getId());
        assertEquals("v1", response.getVer());
        assertNotNull(response.getTs());
        assertNotNull(response.getParams());
    }

    @Test
    @DisplayName("Should have ISO-8601 timestamp format")
    void constructor_Timestamp_IsISO8601Format() {
        ApiResponse response = new ApiResponse();

        String timestamp = response.getTs();
        // Should be parseable as ISO-8601 Instant
        assertDoesNotThrow(() -> Instant.parse(timestamp));
    }

    @Test
    @DisplayName("Should generate unique UUIDs for params")
    void constructor_Params_HasUniqueUUID() {
        ApiResponse response1 = new ApiResponse();
        ApiResponse response2 = new ApiResponse();

        assertNotEquals(
                response1.getParams().getResmsgid(),
                response2.getParams().getResmsgid()
        );
    }

    @Test
    @DisplayName("Should set and get response code")
    void setResponseCode_SetsCorrectly() {
        ApiResponse response = new ApiResponse();
        response.setResponseCode(HttpStatus.OK);

        assertEquals(HttpStatus.OK, response.getResponseCode());
    }

    @Test
    @DisplayName("Should set and get response data")
    void setResponse_SetsDataCorrectly() {
        ApiResponse response = new ApiResponse();
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        data.put("count", 42);

        response.setResponse(data);

        assertEquals("value", response.getResponse().get("key"));
        assertEquals(42, response.getResponse().get("count"));
    }

    @Test
    @DisplayName("Should allow adding data to response map")
    void getResponse_AllowsAddingData() {
        ApiResponse response = new ApiResponse();
        response.getResponse().put("result", "success");
        response.getResponse().put("data", new HashMap<>());

        assertEquals("success", response.getResponse().get("result"));
        assertNotNull(response.getResponse().get("data"));
    }

    @Test
    @DisplayName("Should set error status in params")
    void params_SetErrorStatus() {
        ApiResponse response = new ApiResponse();
        response.getParams().setStatus(CommonConstants.FAILED);
        response.getParams().setErrmsg("Test error message");
        response.getParams().setErr("ERR_CODE");

        assertEquals(CommonConstants.FAILED, response.getParams().getStatus());
        assertEquals("Test error message", response.getParams().getErrmsg());
        assertEquals("ERR_CODE", response.getParams().getErr());
    }

    @Test
    @DisplayName("Should handle HttpStatus UNAUTHORIZED")
    void setResponseCode_Unauthorized_SetsCorrectly() {
        ApiResponse response = new ApiResponse();
        response.setResponseCode(HttpStatus.UNAUTHORIZED);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseCode());
        assertEquals(401, response.getResponseCode().value());
    }

    @Test
    @DisplayName("Should handle HttpStatus INTERNAL_SERVER_ERROR")
    void setResponseCode_InternalServerError_SetsCorrectly() {
        ApiResponse response = new ApiResponse();
        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
        assertEquals(500, response.getResponseCode().value());
    }

    @Test
    @DisplayName("Should set version correctly")
    void setVer_SetsVersionCorrectly() {
        ApiResponse response = new ApiResponse();
        response.setVer("v2");

        assertEquals("v2", response.getVer());
    }

    @Test
    @DisplayName("Should set id correctly")
    void setId_SetsIdCorrectly() {
        ApiResponse response = new ApiResponse();
        response.setId("new.api.id");

        assertEquals("new.api.id", response.getId());
    }

    @Test
    @DisplayName("Params should have matching resmsgid and msgid")
    void constructor_ParamsIds_Match() {
        ApiResponse response = new ApiResponse();

        assertEquals(
                response.getParams().getResmsgid(),
                response.getParams().getMsgid()
        );
    }
}

package org.igot.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiRespParamTest {

    @Test
    @DisplayName("Should create ApiRespParam with default constructor")
    void constructor_Default_CreatesEmptyObject() {
        ApiRespParam param = new ApiRespParam();

        assertNull(param.getResmsgid());
        assertNull(param.getMsgid());
        assertNull(param.getErr());
        assertNull(param.getStatus());
        assertNull(param.getErrmsg());
    }

    @Test
    @DisplayName("Should create ApiRespParam with id constructor")
    void constructor_WithId_SetsResmsgidAndMsgid() {
        String id = "test-message-id";
        ApiRespParam param = new ApiRespParam(id);

        assertEquals(id, param.getResmsgid());
        assertEquals(id, param.getMsgid());
        assertNull(param.getErr());
        assertNull(param.getStatus());
        assertNull(param.getErrmsg());
    }

    @Test
    @DisplayName("Should set and get resmsgid")
    void setResmsgid_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        param.setResmsgid("response-msg-id");

        assertEquals("response-msg-id", param.getResmsgid());
    }

    @Test
    @DisplayName("Should set and get msgid")
    void setMsgid_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        param.setMsgid("message-id");

        assertEquals("message-id", param.getMsgid());
    }

    @Test
    @DisplayName("Should set and get err")
    void setErr_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        param.setErr("ERROR_CODE");

        assertEquals("ERROR_CODE", param.getErr());
    }

    @Test
    @DisplayName("Should set and get status")
    void setStatus_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        param.setStatus("SUCCESS");

        assertEquals("SUCCESS", param.getStatus());
    }

    @Test
    @DisplayName("Should set and get errmsg")
    void setErrmsg_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        param.setErrmsg("Error message description");

        assertEquals("Error message description", param.getErrmsg());
    }

    @Test
    @DisplayName("Should handle FAILED status")
    void setStatus_Failed_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        param.setStatus(CommonConstants.FAILED);

        assertEquals(CommonConstants.FAILED, param.getStatus());
    }

    @Test
    @DisplayName("Should set error details correctly")
    void setErrorDetails_SetsAllErrorFields() {
        ApiRespParam param = new ApiRespParam("test-id");
        param.setStatus("FAILED");
        param.setErr("AUTH_ERROR");
        param.setErrmsg("Authentication failed");

        assertEquals("FAILED", param.getStatus());
        assertEquals("AUTH_ERROR", param.getErr());
        assertEquals("Authentication failed", param.getErrmsg());
        assertEquals("test-id", param.getResmsgid());
        assertEquals("test-id", param.getMsgid());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void setFields_EmptyStrings_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        param.setResmsgid("");
        param.setMsgid("");
        param.setErr("");
        param.setStatus("");
        param.setErrmsg("");

        assertEquals("", param.getResmsgid());
        assertEquals("", param.getMsgid());
        assertEquals("", param.getErr());
        assertEquals("", param.getStatus());
        assertEquals("", param.getErrmsg());
    }

    @Test
    @DisplayName("Should handle null values")
    void setFields_NullValues_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam("initial-id");
        param.setResmsgid(null);
        param.setMsgid(null);
        param.setErr(null);
        param.setStatus(null);
        param.setErrmsg(null);

        assertNull(param.getResmsgid());
        assertNull(param.getMsgid());
        assertNull(param.getErr());
        assertNull(param.getStatus());
        assertNull(param.getErrmsg());
    }

    @Test
    @DisplayName("Should handle long strings")
    void setFields_LongStrings_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        String longString = "A".repeat(1000);

        param.setErrmsg(longString);

        assertEquals(longString, param.getErrmsg());
        assertEquals(1000, param.getErrmsg().length());
    }

    @Test
    @DisplayName("Should handle special characters")
    void setFields_SpecialCharacters_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        String specialChars = "Error: !@#$%^&*()_+-=[]{}|;':\",./<>?";

        param.setErrmsg(specialChars);

        assertEquals(specialChars, param.getErrmsg());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void setFields_Unicode_SetsCorrectly() {
        ApiRespParam param = new ApiRespParam();
        String unicode = "错误信息 🚨 خطأ";

        param.setErrmsg(unicode);

        assertEquals(unicode, param.getErrmsg());
    }
}

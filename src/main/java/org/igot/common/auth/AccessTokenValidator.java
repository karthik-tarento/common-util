package org.igot.common.auth;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.igot.common.ApiResponse;
import org.igot.common.CommonConstants;
import org.igot.common.PropertiesCache;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Validator for access tokens.
 * This class provides functionality to validate access tokens used for
 * authentication and authorization.
 */
@Slf4j
@Component
public class AccessTokenValidator {

    private final ObjectMapper mapper;
    private final PropertiesCache cache;
    private final KeyManager keyManager;

    public AccessTokenValidator(KeyManager keyManager, ObjectMapper mapper, PropertiesCache cache) {
        this.keyManager = keyManager;
        this.mapper = mapper;
        this.cache = cache;
    }

    private Map<String, Object> validateToken(String token) throws Exception {
        try {
            String[] tokenElements = token.split("\\.");
            String header = tokenElements[0];
            String body = tokenElements[1];
            String signature = tokenElements[2];
            String payLoad = header + CommonConstants.DOT_SEPARATOR + body;
            Map<Object, Object> headerData = mapper.readValue(new String(decodeFromBase64(header)), Map.class);
            String keyId = headerData.get("kid").toString();
            KeyData keyData = keyManager.getPublicKey(keyId);
            if (keyData == null) {
                throw new Exception("Public key not found for keyId: " + keyId);
            }
            boolean isValid = CryptoUtil.verifyRSASign(
                    payLoad,
                    decodeFromBase64(signature),
                    keyData.getPublicKey(),
                    CommonConstants.SHA_256_WITH_RSA);
            if (isValid) {
                Map<String, Object> tokenBody = mapper.readValue(new String(decodeFromBase64(body)), Map.class);
                boolean isExp = isExpired((Integer) tokenBody.get("exp"));
                if (isExp) {
                    throw new Exception("Expired auth token is received.");
                }
                return tokenBody;
            } else {
                throw new Exception("Invalid auth token is received.");
            }
        } catch (Exception e) {
            log.warn("Failed to validate the user token. Exception: ", e);
        }
        return Collections.EMPTY_MAP;
    }

    public String verifyUserToken(String token) {
        String userId = CommonConstants._UNAUTHORIZED;
        try {
            Map<String, Object> payload = validateToken(token);
            if (MapUtils.isNotEmpty(payload) && checkIss((String) payload.get("iss"))) {
                userId = (String) payload.get(CommonConstants.SUB);
                if (StringUtils.hasLength(userId)) {
                    int pos = userId.lastIndexOf(":");
                    userId = userId.substring(pos + 1);
                }
            }
        } catch (Exception ex) {
            log.error("Exception in verifyUserAccessToken: verify ", ex);
        }
        return userId;
    }

    private boolean checkIss(String iss) {
        String realmUrl = cache.getProperty(CommonConstants.SSO_URL) + "realms/"
                + cache.getProperty(CommonConstants.SSO_REALM);
        if (!StringUtils.hasLength(realmUrl))
            return false;
        return (realmUrl.equalsIgnoreCase(iss));
    }

    private boolean isExpired(Integer expiration) {
        long currentTime = Instant.now().getEpochSecond();
        boolean retValue = (currentTime > expiration);
        if (retValue) {
            log.warn("Received expired auth token request. Current time: {}, Token expire time: {}",
                    currentTime, expiration);
        }
        return retValue;
    }

    private byte[] decodeFromBase64(String data) {
        return Base64Util.decode(data, 11);
    }

    public String fetchUserIdFromAccessToken(String accessToken) {
        String clientAccessTokenId = null;
        if (accessToken != null) {
            try {
                clientAccessTokenId = verifyUserToken(accessToken);
                if (CommonConstants._UNAUTHORIZED.equalsIgnoreCase(clientAccessTokenId)) {
                    clientAccessTokenId = null;
                }
            } catch (Exception ex) {
                String errMsg = "Exception occurred while fetching the userid from the access token. Exception: "
                        + ex.getMessage();
                log.error(errMsg, ex);
                clientAccessTokenId = null;
            }
        }
        return clientAccessTokenId;
    }

    public String fetchUserIdFromAccessToken(String accessToken, ApiResponse response) {
        String clientAccessTokenId = null;
        if (accessToken != null) {
            try {
                clientAccessTokenId = verifyUserToken(accessToken);
                if (CommonConstants._UNAUTHORIZED.equalsIgnoreCase(clientAccessTokenId)) {
                    response.getParams().setStatus(CommonConstants.FAILED);
                    response.getParams().setErrmsg(CommonConstants.ACCESS_TOKEN_IS_EXPIRED);
                    response.setResponseCode(HttpStatus.UNAUTHORIZED);
                    clientAccessTokenId = null;
                }
            } catch (Exception ex) {
                String errMsg = "Exception occurred while fetching the userid from the access token. Exception: "
                        + ex.getMessage();
                log.error(errMsg, ex);
                response.getParams().setStatus(CommonConstants.FAILED);
                response.getParams().setErrmsg(CommonConstants.ACCESS_TOKEN_VALIDATION_FAILED);
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
                clientAccessTokenId = null;
            }
        }
        return clientAccessTokenId;
    }

    public Map<String, Object> extractTokenPayload(String token) {
        Map<String, Object> tokenPayload = new HashMap<>();
        try {
            Map<String, Object> payload = validateToken(token);
            if (MapUtils.isNotEmpty(payload) && checkIss((String) payload.get("iss"))) {
                tokenPayload = payload;
            }
        } catch (Exception ex) {
            log.error("Exception in extractTokenPayload: ", ex);
        }
        return tokenPayload;
    }
}

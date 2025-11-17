package org.igot.common.auth;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.igot.common.CommonConstants;
import org.igot.common.PropertiesCache;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KeyManager {

    private final PropertiesCache propertiesCache;
    private final Map<String, KeyData> keyMap = new HashMap<>();

    public KeyManager(PropertiesCache propertiesCache) {
        this.propertiesCache = propertiesCache;
    }

    @PostConstruct
    public void init() {
        String basePath = propertiesCache.getProperty(CommonConstants.ACCESS_TOKEN_PUBLICKEY_BASEPATH);
        try (Stream<Path> walk = Files.walk(Paths.get(basePath))) {
            List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
            result.forEach(
                    file -> {
                        try {
                            Path path = Paths.get(file);
                            String content = Files.readString(path, StandardCharsets.UTF_8);
                            KeyData keyData = new KeyData(
                                    path.getFileName().toString(),
                                    loadPublicKey(content));
                            keyMap.put(path.getFileName().toString(), keyData);
                        } catch (Exception e) {
                            log.error("KeyManager:init: exception in reading public keys ", e);
                        }
                    });
        } catch (Exception e) {
            log.error("KeyManager:init: exception in loading publickeys ", e);
        }
    }

    public KeyData getPublicKey(String keyId) {
        KeyData keyData = keyMap.get(keyId);
        if (keyData == null) {
            log.warn("Public key not found for keyId: {}", keyId);
        }
        return keyData;
    }

    public static PublicKey loadPublicKey(String key) throws Exception {
        String publicKey = key
                .replaceAll("(-+BEGIN PUBLIC KEY-+)", "")
                .replaceAll("(-+END PUBLIC KEY-+)", "")
                .replaceAll("[\\r\\n]+", "");

        byte[] keyBytes = Base64Util.decode(publicKey, 0);

        X509EncodedKeySpec x509publicKey = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(x509publicKey);
    }
}
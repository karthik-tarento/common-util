package org.igot.common.auth;

import java.security.PublicKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KeyData {
    private String keyId;
    private PublicKey publicKey;
}

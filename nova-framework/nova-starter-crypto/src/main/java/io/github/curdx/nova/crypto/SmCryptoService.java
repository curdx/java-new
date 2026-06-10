package io.github.curdx.nova.crypto;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;

/**
 * 国密算法门面（基于 BouncyCastle，经 Hutool 封装）。
 * SM3 摘要、SM4 对称加解密；SM2 签名/加密在密钥管理方案落地后开放。
 */
public class SmCryptoService {

    private final SM4 sm4;

    public SmCryptoService(String sm4KeyHex) {
        this.sm4 = new SM4(HexUtil.decodeHex(sm4KeyHex));
    }

    public String sm3(String content) {
        return SmUtil.sm3(content);
    }

    public String sm4EncryptHex(String plain) {
        return sm4.encryptHex(plain, StandardCharsets.UTF_8);
    }

    public String sm4DecryptStr(String cipherHex) {
        return sm4.decryptStr(cipherHex, StandardCharsets.UTF_8);
    }

    SM4 sm4() {
        return sm4;
    }
}

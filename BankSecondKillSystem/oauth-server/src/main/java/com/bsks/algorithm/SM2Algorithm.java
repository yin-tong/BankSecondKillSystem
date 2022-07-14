package com.bsks.algorithm;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bsks.utils.SM2Utils;
import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * 自定义SM2算法，用用于创建jwt
 */
public class SM2Algorithm extends Algorithm {

    public SM2Algorithm(String name, String description) {
        super(name, description);
    }

    /**
     * 签名
     * @param contentBytes
     * @return
     * @throws SignatureGenerationException
     */
    @SneakyThrows
    @Override
    public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
        return SM2Utils.sign(contentBytes);
    }

    /**
     * 验签
     * @param jwt
     * @throws SignatureVerificationException
     */
    @SneakyThrows
    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {

        //获取header和payload处的数据
        String headerData = new String(Base64.decodeBase64(jwt.getHeader()));
        String payloadData = new String(Base64.decodeBase64(jwt.getPayload()));

        //将数据还原成jwt的header 和 payload
        String header = Base64.encodeBase64URLSafeString(headerData.getBytes(StandardCharsets.UTF_8));
        String payload = Base64.encodeBase64URLSafeString(payloadData.getBytes(StandardCharsets.UTF_8));
        String content = header+"."+payload;
        byte[] signatureBytes = Base64.decodeBase64URLSafe(jwt.getSignature());
        boolean valid = SM2Utils.verify(content.getBytes(StandardCharsets.UTF_8),signatureBytes) ;
        if (!valid) {
            throw new SignatureVerificationException(this);
        }
    }
}

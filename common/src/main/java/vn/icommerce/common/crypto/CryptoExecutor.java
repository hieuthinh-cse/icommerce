package vn.icommerce.common.crypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
@Getter
public class CryptoExecutor {

  private PrivateKey privateKey;

  private PublicKey publicKey;

  private Cipher encryptCipher;

  private Cipher decryptCipher;

  public CryptoExecutor init() {
    BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
    if (Objects.isNull(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME))) {
      Security.addProvider(bouncyCastleProvider);
    }
    return this;
  }

  @SneakyThrows
  public CryptoExecutor setUpRsaPrivateKey(String privateKey) {

    this.privateKey = KeyFactory
        .getInstance("RSA")
        .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    return this;
  }

  @SneakyThrows
  public CryptoExecutor setUpRsaPublicKey(String publicKey) {
    this.publicKey = KeyFactory
        .getInstance("RSA", "BC")
        .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
    return this;
  }

  @SneakyThrows
  public CryptoExecutor setUp3DesKey(String secretKey) {
    var secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "DESede");

    encryptCipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
    encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

    decryptCipher = Cipher.getInstance("DESede/ECB/PKCS7Padding", "BC");
    decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

    return this;
  }

  @SneakyThrows
  public String signWithRsa(String data, String algorithm) {
    if (Objects.isNull(privateKey)) {
      throw new RuntimeException("Private key was not provided");
    }
    var signatureEngine = Signature.getInstance(algorithm);
    signatureEngine.initSign(privateKey);
    signatureEngine.update(data.getBytes());

    return Base64.getEncoder().encodeToString(signatureEngine.sign());
  }

  @SneakyThrows
  public boolean verifyWithRsa(String data, String signature, String algorithm) {
    if (Objects.isNull(publicKey)) {
      throw new RuntimeException("Public key was not provided");
    }
    var signatureEngine = Signature.getInstance(algorithm);
    signatureEngine.initVerify(publicKey);
    signatureEngine.update(data.getBytes());

    return signatureEngine.verify(Base64.getDecoder().decode(signature));
  }

  @SneakyThrows
  public String encryptWith3Des(String data) {
    if (Objects.isNull(encryptCipher)) {
      throw new RuntimeException("Secret key was not provided");
    }

    var cipherText = encryptCipher.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(cipherText);
  }

  @SneakyThrows
  public byte[] decryptWith3Des(String key, String cipherText) {
    if (Objects.isNull(decryptCipher)) {
      throw new RuntimeException("Secret key was not provided");
    }

    var inputBytes = Base64.getDecoder().decode(cipherText.getBytes());
    return decryptCipher.doFinal(inputBytes);
  }

  public String getMd5(String data) {
    return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
  }

}

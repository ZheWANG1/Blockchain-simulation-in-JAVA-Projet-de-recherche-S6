package PoW;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * Util class for generating key pairs with methods for generating and verifying signatures.
 */
public class RsaUtil {

    /**
     * Method of generating key pair
     *
     * @return Key pair of 512 bits
     * @throws Exception Exception
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512);
        return generator.generateKeyPair();
    }

    /**
     * Method of generating signature by private key
     *
     * @param plainText  Plaintext that needs to be encrypted
     * @param privateKey The sender's private key
     * @return Ciphertext
     * @throws Exception Exception
     */
    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        // Sender privateKey
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * Method of verifying signature by public key
     *
     * @param plainText Plaintext that needs to be validated
     * @param signature The ciphertext belonging to this plaintext
     * @param publicKey The sender's public key
     * @return Whether it was sent by the sender himself
     * @throws Exception Exception
     */
    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }


}

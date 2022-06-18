package com.stelpolvo.video.service.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {

	private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2qk/blKZBiJ60fRbZmaSAoylYgA9otbhtNhGIxqAAl3adi6MocNJ1fnlu1Yr/3OXYdbpLe0gYv8ZiFpaprThNswBkpOlmXwMTqNkJOV3cNhGcvqQZDF+y4w1Bxc5lTdvaicKTq5OTX+b3lJ2ntrAxOInZfKsPdvyh69sKlcTeXQIDAQAB";

	private static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALaqT9uUpkGInrR9FtmZpICjKViAD2i1uG02EYjGoACXdp2Loyhw0nV+eW7Viv/c5dh1ukt7SBi/xmIWlqmtOE2zAGSk6WZfAxOo2Qk5Xdw2EZy+pBkMX7LjDUHFzmVN29qJwpOrk5Nf5veUnae2sDE4idl8qw92/KHr2wqVxN5dAgMBAAECgYEAlAJWaNb/DBTWe+rry+/Jso4Cn3pMggzU3nE0Lf67gR85dpjB4K/iaF/Np3gShBYKi6WBCU5gceVaqvogpGX+OuCsR0toLvY0vX6VjuGZA4OIK4v76uVQRDbROWLQIvztUKx93g9PDDLZJgQFOxWktSVzkNFJa1GH8d9Zh6V57MUCQQDdBiI/bU9Nfo1JI5YQi4HjP97/PoUo+ihgelCIDc3Q2X+haAElFV23pYHGTY0sX+z7GFV+6wE6uNDEXqabeFSPAkEA05I8Ya3Fjl5jirhKuL9uOd5GY2JmvrgUoYa/4Ddbwg+oXVt4oIec0Yem57ep/TzyG2BGaSgtdckhSNRe2mlMUwJBAMLcLIeE5JyqzP9Fa4EMraffYMfho957pethCuzYVrMhfh+cJR4/lw9Y0HA3YRZVQAYj7wRlRgEyfYQS+ooo7UsCQAb57nXGFhqQsDWVyBj5bvgHhUinqP2m8j81Q0Rwbt0iSQnzBzI75obeMzm2y6snBFC0xv2WrpEQtcFCB3KqQDMCQQC9YZXehEjJuIbnMmO5LuVYk+2e65HOcKn1VpUL5F9UMzf78D7poOpp1y6dPtfkU0BQW8WyQk3qO8bos7oAkYAP";

	public static String getPublicKeyStr(){
		return PUBLIC_KEY;
	}

	public static RSAPublicKey getPublicKey() throws Exception {
		byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
		return (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
	}

	public static RSAPrivateKey getPrivateKey() throws Exception {
		byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
		return (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
	}
	
	public static RSAKey generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
		String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
		return new RSAKey(privateKey, privateKeyString, publicKey, publicKeyString);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(RSAUtil.encrypt("123"));
	}
	public static String encrypt(String source) throws Exception {
		byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
		RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(1, rsaPublicKey);
		return Base64.encodeBase64String(cipher.doFinal(source.getBytes(StandardCharsets.UTF_8)));
	}

	public static Cipher getCipher() throws Exception {
		byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(2, rsaPrivateKey);
		return cipher;
	}

	public static String decrypt(String text) throws Exception {
		Cipher cipher = getCipher();
		byte[] inputByte = Base64.decodeBase64(text.getBytes(StandardCharsets.UTF_8));
		return new String(cipher.doFinal(inputByte));
	}
	
	public static class RSAKey {
		  private RSAPrivateKey privateKey;
		  private String privateKeyString;
		  private RSAPublicKey publicKey;
		  public String publicKeyString;

		  public RSAKey(RSAPrivateKey privateKey, String privateKeyString, RSAPublicKey publicKey, String publicKeyString) {
		    this.privateKey = privateKey;
		    this.privateKeyString = privateKeyString;
		    this.publicKey = publicKey;
		    this.publicKeyString = publicKeyString;
		  }

		  public RSAPrivateKey getPrivateKey() {
		    return this.privateKey;
		  }

		  public void setPrivateKey(RSAPrivateKey privateKey) {
		    this.privateKey = privateKey;
		  }

		  public String getPrivateKeyString() {
		    return this.privateKeyString;
		  }

		  public void setPrivateKeyString(String privateKeyString) {
		    this.privateKeyString = privateKeyString;
		  }

		  public RSAPublicKey getPublicKey() {
		    return this.publicKey;
		  }

		  public void setPublicKey(RSAPublicKey publicKey) {
		    this.publicKey = publicKey;
		  }

		  public String getPublicKeyString() {
		    return this.publicKeyString;
		  }

		  public void setPublicKeyString(String publicKeyString) {
		    this.publicKeyString = publicKeyString;
		  }
		}
}
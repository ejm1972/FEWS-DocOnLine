package ar.com.coninf.doconline.business.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncriptacionUtiles {

	public static byte[] computeHash(String x) throws NoSuchAlgorithmException {
		java.security.MessageDigest d1 = null;
		d1 = java.security.MessageDigest.getInstance("SHA-512");
		d1.reset();
		d1.update(x.getBytes());
		byte[] b1 = d1.digest();
		return b1;
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	public static String encryptMD5(String password)
			throws NoSuchAlgorithmException {
		String md5val = "";
		MessageDigest algorithm = null;

		algorithm = MessageDigest.getInstance("MD5");

		byte[] defaultBytes = password.getBytes();
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < messageDigest.length; i++) {
			String hex = Integer.toHexString(0xFF & messageDigest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		md5val = hexString.toString();
		return md5val;
	}

}

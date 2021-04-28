package jp.co.tokyo_gas.cirius_fw.application;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 暗号関連の処理をまとめたクラス.
 * 
 * @author k-jyoumura@tg-inet.co.jp
 * @version 2016.08.10
 */
public final class CryptUtil {

	/**
	 * ログ出力インスタンス.
	 */
	private static final Logger LOG = Logger.getLogger("CryptUtil");

	/**
	 * アルゴリズム.
	 */
	static final String ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * 秘密鍵.
	 */
	private static final SecretKeySpec SECRET = new SecretKeySpec("cirius2018cirius".getBytes(), 0,
		16, "AES");

	/**
	 * コンストラクタ.
	 */
	private CryptUtil() {
	}

	/**
	 * 暗号化.
	 * 
	 * @param originalStr 元の文字列
	 * @return 暗号化後の文字列
	 */
	public static String encrypt(final String originalStr) {
		if (null == originalStr) {
			return "";
		}
		byte[] original = originalStr.getBytes();
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, SECRET);
			byte[] encrypted = cipher.doFinal(original);
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
			| IllegalBlockSizeException | BadPaddingException ex) {
			LOG.warning(ex.getMessage());
			return "";
		}
	}

	/**
	 * 復号.
	 * 
	 * @param encryptedStr 暗号化文字列
	 * @return 復号後の文字列
	 */
	public static String decrypt(final String encryptedStr) {
		if (null == encryptedStr) {
			return "";
		}
		byte[] encrypted = Base64.getDecoder().decode(encryptedStr.getBytes());
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, SECRET);
			byte[] original = cipher.doFinal(encrypted);
			return new String(original);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
			| IllegalBlockSizeException | BadPaddingException ex) {
			LOG.warning(ex.getMessage());
			return "";
		}
	}

}

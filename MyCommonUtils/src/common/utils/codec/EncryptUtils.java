package common.utils.codec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年4月23日 下午5:22:38
 * @description:加密解密
 * @version :0.1
 */

public class EncryptUtils {
	private static final String key = "$_2015YoHuU";
	private static byte keyChar[];
	static {
		keyChar = key.getBytes();
	}

	public static String encode(String data) {
		byte[] srcByte = data.getBytes();

		byte[] dscChar = encodeByte(srcByte);
		
		return Base64.encodeBase64URLSafeString(dscChar);
	}

	private static byte[] encodeByte(byte[] srcByte) {

		int len = srcByte.length;
		byte[] dscChar = new byte[len];

		int keyIndex = keyChar.length;

		for (int i = 0; i < len; i++) {
			dscChar[i] = (byte) (srcByte[i] ^ keyChar[i % keyIndex]);
		}
		return dscChar;
	}

	public static String decode(String data) {

		byte[] dscByte = encodeByte(Base64.decodeBase64(data));

		return new String(dscByte);
	}

	public static void main(String[] args) {
		String str = "{\"mkId\":1001,\"sId\":2}";
		System.out.println("原始的:" + str);
		
		String codeStr = EncryptUtils.encode(str);
		System.out.println("加密后:" + codeStr+"end");

		codeStr = EncryptUtils.decode(codeStr);
		System.out.println("解密后:" + codeStr);

	}

}

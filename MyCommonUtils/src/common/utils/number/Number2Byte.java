package common.utils.number;

import org.apache.commons.codec.binary.Base64;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年3月18日 下午1:16:19
 * @description:
 * @version :0.1
 */

public class Number2Byte {

	public static byte[] int2Bytes(int num) {
		byte[] byteNum = new byte[4];
		for (int ix = 0; ix < 4; ++ix) {
			int offset = 32 - (ix + 1) * 8;
			byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	public static int bytes2Int(byte[] byteNum) {
		int num = 0;
		for (int ix = 0; ix < 4; ++ix) {
			num <<= 8;
			num |= (byteNum[ix] & 0xff);
		}
		return num;
	}

	public static byte[] long2Bytes(long num) {
		byte[] byteNum = new byte[8];
		for (int ix = 0; ix < 8; ++ix) {
			int offset = 64 - (ix + 1) * 8;
			byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	public static long bytes2Long(byte[] byteNum) {
		long num = 0;
		for (int ix = 0; ix < 8; ++ix) {
			num <<= 8;
			num |= (byteNum[ix] & 0xff);
		}
		return num;
	}

	public static void main(String[] args) {
		long a = 00000000L;
		a = a + 32;

		String num = String.format("%08d", a);

		System.out.println(Long.parseLong(num));
		num = num + "abcd";
		System.out.println(num);
		System.out.println(num.substring(0, 8));
		System.out.println(num.substring(8));
	}
}

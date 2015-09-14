package common.utils.codec;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年9月14日 下午3:24:12
 * @description:
 * @version :0.1
 */

public class HttpCodec {
	private static Logger LOG = LoggerFactory.getLogger(HttpCodec.class);
	private static String key = "$_YzC%2015";

	/**
	 * 封装URL+time+sign
	 * 
	 * @param url
	 * @return
	 */
	public static String wrapperUrl(String url) {

		if (url.indexOf("?") != -1) {
			url = url + "&time=" + System.currentTimeMillis() / 1000;
			url = url + "&sign=" + getSign(url);

		} else {
			LOG.error("url 参数不能为空:{}", url);
			return url;
		}
		return url;
	}
	/**
	 * 检查sign是否正确
	 * @param url
	 * @return
	 */
	public static boolean checkSign(String url) {
		try {
			URL urlObj = new URL(url);

			String[] paramArr = urlObj.getQuery().split("&");

			Map<String, String> paramMap = new HashMap<String, String>();

			StringBuffer paramNoSign = new StringBuffer();
			for (String p : paramArr) {
				String paramObj[] = p.split("=");
				paramMap.put(paramObj[0], paramObj[1]);

				if (!paramObj[0].equals("sign")) {
					paramNoSign.append(p).append("&");
				}
			}
			paramNoSign.delete(paramNoSign.length() - 1, paramNoSign.length());

			// 判断时间有效期
			String time = paramMap.get("time");
			if (time != null) {
				long stime = Long.parseLong(time);
				if (System.currentTimeMillis() / 1000 - stime < 300) {// 5分钟内有效

					String ssign = paramMap.get("sign");

					String surl = url.substring(0, url.indexOf("?") + 1) + paramNoSign.toString();

					String nsign = getSign(surl);

					return ssign.equals(nsign);

				} else {
					LOG.error("sign时间过期:{}", url);
				}
			} else {
				LOG.error("参数异常,没有time参数:{}", url);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成sign time,和sign为必要参数
	 * 
	 * @param url
	 * @return
	 */
	private static String getSign(String url) {

		try {
			URL urlObj = new URL(url);

			if (urlObj.getQuery() != null && urlObj.getQuery() != "") {
				String urlQuery = urlObj.getQuery() + "&key=" + key;

				String paramArr[] = urlQuery.split("&");
				Arrays.sort(paramArr);

				StringBuffer sb = new StringBuffer();

				for (String p : paramArr) {
					sb.append(p).append("&");
				}
				sb.delete(sb.length() - 1, sb.length());

				return DigestUtils.md5Hex(sb.toString());
			} else {
				LOG.error("url 参数不能为空:{}", url);
			}

			return null;
		} catch (MalformedURLException e) {
			LOG.error("{}", e);
		}
		return null;
	}

	public static void main(String[] args) throws MalformedURLException {
		String url = "http://192.168.0.201:6060/a.action?marketId=10001&serverId=1001&time=1291105518&from=center&sign=abcc";
		// String sign = HttpCodec.getSign("http://192.168.0.201:6060/a.action?marketId=10001&serverId=1001&time=1291105518&from=center&sign=abcc");
		// System.out.println(sign);

		String warpperUrl = wrapperUrl("http://192.168.0.201:6060/a.action?marketId=10001");
		System.out.println(warpperUrl);

		System.out.println(checkSign(warpperUrl));
	}
}

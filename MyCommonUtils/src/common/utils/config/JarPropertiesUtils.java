package common.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月9日 下午4:41:41
 * @description:加载jar包中的properties 文件
 * @version :0.1
 */

public class JarPropertiesUtils {
	/**
	 * 
	 * @param jarNamePath
	 *            jar 的绝对路径，例:C:/xxxx/test.jar
	 * @param fileNamePath
	 *            文件所在JAR 包中所处相对路径,例: /config/log4j.properties
	 * @return
	 */
	public static Properties readProperties(String jarNamePath, String fileNamePath) {

		Properties properties = new Properties();
		InputStream in = null;
		try {

			URL url = new URL("jar:file:" + jarNamePath + "!/" + fileNamePath);
			JarURLConnection jarConnection = (JarURLConnection) url.openConnection();

			in = jarConnection.getInputStream();

			properties.load(in);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return properties;
	}

}

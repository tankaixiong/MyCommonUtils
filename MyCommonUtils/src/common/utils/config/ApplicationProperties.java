package common.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @date:2015年2月6日 上午10:35:05
 * @description:加载配置文件application.properties数据 到map中
 * @version :0.1
 */

public class ApplicationProperties {

	private Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

	private static ApplicationProperties context;

	private ApplicationProperties() {

	}

	/**
	 * 得到单例对象
	 * 
	 * @return
	 */
	public static ApplicationProperties getInstance() {
		if (context == null) {
			synchronized (ApplicationProperties.class) {
				if (context == null) {
					context = new ApplicationProperties();
					context.loadProperties();
				}

			}
		}
		return context;
	}

	/**
	 * 加载配置文件数据到map中
	 */
	public void loadProperties() {
		Properties properties = new Properties();
		InputStream in = ApplicationProperties.class.getResourceAsStream("/application.properties");

		try {
			properties.load(in);
			Enumeration<Object> keys = properties.keys();
			while (keys != null && keys.hasMoreElements()) {
				String key = String.valueOf(keys.nextElement());
				if (key != null) {
					this.putProperty(key, properties.getProperty(key));
				}
			}
		} catch (IOException e) {
			logger.error("初始化系统配置数据:{}", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}
	}

	/**
	 * 存储系统配置相关信息
	 */
	private Map<String, String> configMap = new HashMap<String, String>();

	public void putProperty(String key, String value) {
		configMap.put(key, value);
	}

	public String getProperty(String key) {
		return configMap.get(key);
	}

}

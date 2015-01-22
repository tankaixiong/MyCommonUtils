package common.utils.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @date:Sep 26, 2011 9:03:29 PM
 * @description: 纯文本文件操作类 .txt
 * @version :
 */
public class FileHelper {
	private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

	public String readTXT(String path) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String data = null;
			StringBuffer sbf = new StringBuffer();
			while ((data = br.readLine()) != null) {
				sbf.append(data);
			}

			return sbf.toString();
		} catch (FileNotFoundException e) {
			logger.error("{}", e);
		} catch (IOException e) {
			logger.error("{}", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("{}", e);
				}
			}

		}
		return null;
	}

	public boolean writeTXT(String path, String writeContext) {
		OutputStreamWriter fw = null;
		try {

			fw = new OutputStreamWriter(new FileOutputStream(path), "utf-8");

			fw.write(writeContext, 0, writeContext.length());
			fw.flush();

			return true;
		} catch (IOException e) {
			logger.error("{}", e);
			return false;
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}

	}

	public boolean appendTXT(String path, String writeContext) {
		OutputStreamWriter fw = null;
		try {

			fw = new OutputStreamWriter(new FileOutputStream(path), "utf-8");

			fw.append(writeContext, 0, writeContext.length());
			fw.flush();

			return true;
		} catch (IOException e) {
			logger.error("{}", e);
			return false;
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}

	}

}

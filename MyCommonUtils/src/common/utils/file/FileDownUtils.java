package common.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年6月9日 上午10:18:38
 * @description:文件下载(网络上的资源下载到本地)
 * @version :0.1
 */

public class FileDownUtils {

	private static Logger logger = LoggerFactory.getLogger(FileDownUtils.class);
	
	public static void downloadFile(String srcPath, String descPath) {
		
		descPath=descPath.replaceAll("\\\\", "/");
		
		File file=new File(descPath);
		 
		if(file.isDirectory()){
			if(!file.exists()){
				file.mkdir();
			}
			String fileName=srcPath.substring(srcPath.lastIndexOf("/")+1);
			if(descPath.endsWith("/")){
				descPath+=fileName;
			}else{
				descPath+="/"+fileName;
			}
			
		}else{
			if(file.exists()){
				logger.warn("文件:{} 已经存在，将会覆盖!",descPath);
			}
		}
		logger.info("本地保存路径{}",descPath);
		
		downToLocal(srcPath,descPath);
	}
	 

	public static void downToLocal(String srcPath, String descPath) {
		// 下载网络文件
		int byteread = 0;

		FileOutputStream fs = null;
		InputStream inStream = null;
		try {
			URL url = new URL(srcPath);

			URLConnection conn = url.openConnection();
			inStream = conn.getInputStream();
			fs = new FileOutputStream(descPath);

			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}

		} catch (FileNotFoundException e) {
			logger.error("{}", e);
		} catch (IOException e) {
			logger.error("{}", e);
		} finally {
			try {
				if (fs != null) {
					fs.flush();
					fs.close();
				}
			} catch (IOException e) {
				logger.error("{}", e);
			}
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				logger.error("{}", e);
			}

		}
	}

	public static void main(String[] args) {
		downloadFile("http://files.cnblogs.com/files/tankaixiong/config.zip", "D:\\temp");
		System.out.println("end");
	}

}

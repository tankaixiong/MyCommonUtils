package common.utils.http;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年8月21日 上午10:46:15
 * @description:
 * @version :0.1
 */

public class HttpClientUtils {

	private static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);

	public static String httpGet(String url) {

		GetMethod fileGet = new GetMethod(url); // 若没有commons-codec-1.4-bin.zip, 这里将会出错
		InputStream in = null;
		try {

			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			int status = client.executeMethod(fileGet);
			if (status == HttpStatus.SC_OK) {
				LOG.info("调用成功:{}", status);
				// 上传成功
			} else {
				LOG.info("调用失败{}", status);
				// 上传失败
			}
			in = fileGet.getResponseBodyAsStream();// getResponseBodyAsStream
			String json = IOUtils.toString(in);

			// byte[] responseBody = filePost.getResponseBody();
			// String json = new String(responseBody, "utf-8");

			return json;

		} catch (Exception ex) {
			LOG.error("{}", ex);
		} finally {
			IOUtils.closeQuietly(in);

			fileGet.releaseConnection();
		}

		return null;
	}

	public static String httpPost(String url, Map<String, String> params) {

		PostMethod filePost = new PostMethod(url); // 若没有commons-codec-1.4-bin.zip, 这里将会出错
		InputStream in = null;
		try {

			if (params != null && !params.isEmpty()) {
				Iterator<Map.Entry<String, String>> mapIt = params.entrySet().iterator();
				while (mapIt.hasNext()) {
					Map.Entry<String, String> entry = mapIt.next();
					filePost.addParameter(entry.getKey(), entry.getValue());
				}
			}

			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				LOG.info("调用成功:{}", status);
				// 上传成功
			} else {
				LOG.info("调用失败{}", status);
				// 上传失败
			}
			in = filePost.getResponseBodyAsStream();// getResponseBodyAsStream
			String json = IOUtils.toString(in);

			// byte[] responseBody = filePost.getResponseBody();
			// String json = new String(responseBody, "utf-8");

			return json;// JsonUtils.toBean(json, String[].class);

		} catch (Exception ex) {
			LOG.error("{}", ex);
		} finally {
			IOUtils.closeQuietly(in);

			filePost.releaseConnection();
		}

		return null;
	}

	/**
	 * http 文件上传
	 * 
	 * @param url
	 * @param file
	 * @param params
	 * @return
	 */
	public static String httpMultipart(String url, File file[], Map<String, String> params) {

		PostMethod filePost = new PostMethod(url); // 若没有commons-codec-1.4-bin.zip, 这里将会出错
		InputStream in = null;
		try {
			List<Part> partList = new ArrayList<Part>();

			for (File f : file) {
				partList.add(new FilePart("file", f));
			}

			if (params != null && !params.isEmpty()) {
				Iterator<Map.Entry<String, String>> mapIt = params.entrySet().iterator();
				while (mapIt.hasNext()) {
					Map.Entry<String, String> entry = mapIt.next();
					partList.add(new StringPart(entry.getKey(), entry.getValue()));
				}
			}

			filePost.setRequestEntity(new MultipartRequestEntity(partList.toArray(new Part[partList.size()]), filePost.getParams()));

			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				LOG.info("上传成功:{}", status);
				// 上传成功
			} else {
				LOG.info("上传失败{}", status);
				// 上传失败
			}
			in = filePost.getResponseBodyAsStream();// getResponseBodyAsStream
			String json = IOUtils.toString(in);

			// byte[] responseBody = filePost.getResponseBody();
			// String json = new String(responseBody, "utf-8");

			return json;// JsonUtils.toBean(json, String[].class);

		} catch (Exception ex) {
			LOG.error("{}", ex);
		} finally {
			IOUtils.closeQuietly(in);

			filePost.releaseConnection();
		}

		return null;
	}

}

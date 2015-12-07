package common.utils.json;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年11月5日 下午6:08:29
 * @description:
 * @version :0.1
 */

public class TestJson {

	public static void main(String[] args) {

		try {
			TempPojo pojo = new TempPojo();
			pojo.setId(1L);
			pojo.setName("tank");
			pojo.setRemark("测试");
			pojo.setData(new ArrayList<String>());
			
			 

			String json = JsonUtils.objectMapper.writerWithView(JsonViewEntity.SimpleField.class).writeValueAsString(pojo);
			System.out.println(json);

			json = JsonUtils.objectMapper.writerWithView(JsonViewEntity.BaseField.class).writeValueAsString(pojo);
			System.out.println(json);
			

			pojo = JsonUtils.objectMapper.readValue(json,TempPojo.class);
			System.out.println(pojo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

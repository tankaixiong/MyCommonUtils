package common.utils.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年11月5日 下午6:06:03
 * @description:
 * @version :0.1
 */

public class TempPojo {
	@JsonView(JsonViewEntity.BaseField.class)
	private Long id;
	@JsonView(JsonViewEntity.BaseField.class)
	private String name;

	private String remark;

	@JsonView(JsonViewEntity.SimpleField.class)
	private List<String> data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

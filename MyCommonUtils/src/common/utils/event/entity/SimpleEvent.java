package common.utils.event.entity;

import java.util.Arrays;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年12月3日 下午5:23:38
 * @description:
 * @version :0.1
 */

public class SimpleEvent {

	public SimpleEvent() {

	}

	// public SimpleEvent(String name, Object[] params) {
	// this.name = name;
	// this.params = params;
	// }
	public SimpleEvent(String name, Object... params) {
		this.name = name;
		this.params = params;
	}

	private String name;
	private Object[] params;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "SimpleEvent [name=" + name + ", params=" + Arrays.toString(params) + "]";
	}

}

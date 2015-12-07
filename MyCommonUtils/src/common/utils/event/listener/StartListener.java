package common.utils.event.listener;

import common.utils.event.entity.SimpleEvent;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年12月4日 下午2:44:08
 * @description:
 * @version :0.1
 */

public class StartListener implements BaseEventListener<SimpleEvent> {

	@Override
	public String getListenerEventName() {
		return "start";
	}

	@Override
	public void onFireEvent(SimpleEvent event) {
		 System.out.println(event);
	}

}

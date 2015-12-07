package common.utils.event;

import common.utils.bee.ThreadBeeRedisQueue;
import common.utils.event.entity.SimpleEvent;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年12月4日 下午4:26:26
 * @description:
 * @version :0.1
 */

public class EventThreadBee extends ThreadBeeRedisQueue<SimpleEvent> {
	private static EventThreadBee contex = new EventThreadBee(2, "bee:redis");

	public static EventThreadBee getInstance() {
		return contex;
	}

	public EventThreadBee(int size, String key) {
		super(size, key);
	}

	@Override
	public void runBee(SimpleEvent t) {
		 
		EventManager.fireEvent(t);

	}

}

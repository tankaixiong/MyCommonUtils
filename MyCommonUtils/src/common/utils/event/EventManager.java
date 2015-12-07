package common.utils.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.joran.event.StartEvent;
import common.utils.event.entity.SimpleEvent;
import common.utils.event.listener.BaseEventListener;
import common.utils.event.listener.StartListener;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年12月4日 下午2:02:10
 * @description:
 * @version :0.1
 */

public class EventManager {
	private static org.slf4j.Logger LOG = LoggerFactory.getLogger(EventManager.class);

	private static Map<String, List<BaseEventListener>> eventListenerMap = new HashMap<String, List<BaseEventListener>>();

	public static void addListenter(BaseEventListener listener) {
		if (listener != null && listener.getListenerEventName() != null && listener.getListenerEventName() != "") {
			List<BaseEventListener> list = eventListenerMap.get(listener.getListenerEventName());
			if (list == null) {
				list = new ArrayList<BaseEventListener>();
			}
			list.add(listener);
			eventListenerMap.put(listener.getListenerEventName(), list);
		} else {
			LOG.error("监听对象异常 :{}", listener);
		}

	}

	public static void fireEvent(SimpleEvent event) {
		if (event != null && event.getName() != null) {
			List<BaseEventListener> eventListenerList = eventListenerMap.get(event.getName());
			if (eventListenerList != null && !eventListenerList.isEmpty()) {
				Iterator<BaseEventListener> listenerIt = eventListenerList.iterator();
				while (listenerIt.hasNext()) {
					BaseEventListener item = listenerIt.next();

					item.onFireEvent(event);

				}
			} else {
				LOG.error("没有找到事件监听 :{}", event);
			}
		}

	}

	public static void main(String[] args) {

		Type[] type = StartListener.class.getGenericInterfaces();

		Type trueType = ((ParameterizedType) type[0]).getActualTypeArguments()[0];

		Class clazz = (Class) trueType;
		System.out.println(clazz == StartEvent.class);
	}

}

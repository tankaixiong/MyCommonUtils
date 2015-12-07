package common.utils.event.listener;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年12月3日 下午5:26:41
 * @description:
 * @version :0.1
 */

public interface BaseEventListener<T> {

	String getListenerEventName();

	void onFireEvent(T event);
}

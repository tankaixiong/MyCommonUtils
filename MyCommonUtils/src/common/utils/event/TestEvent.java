package common.utils.event;

import java.util.Scanner;

import common.utils.event.entity.SimpleEvent;
import common.utils.event.listener.StartListener;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年12月4日 下午6:05:35
 * @description:
 * @version :0.1
 */

public class TestEvent {

	public static void main(String[] args) {
		
		EventManager.addListenter(new StartListener());
		
		EventThreadBee.getInstance().push(new SimpleEvent("start","测试"));
		
		 Scanner scan = new Scanner(System.in);  
		 scan.nextLine();

	}

}

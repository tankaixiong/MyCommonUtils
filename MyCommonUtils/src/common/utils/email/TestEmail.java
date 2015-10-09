package common.utils.email;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年10月1日 下午3:26:13
 * @description:
 * @version :0.1
 */

public class TestEmail {
	public static void main(String[] args) {

		SendEmail sender = new SendEmail();
		sender.send("程序异常", "程序异常,测试一下"+getRandomBody(), "314032551@qq.com");

	}

	public static String getRandomBody() {
		String[] body = new String[] { "发现程序员经常熬夜有三个弊端：第一，记忆力越来越差；第二，数数经常会数错；第四，记忆力越来越差。","程序猿的读书历程：x语言入门—>x语言应用实践—>x语言高阶编程—>x语言的科学与艺术—>编程之美—>编程之道—>编程之禅—>颈椎病康复指南。","程序猿最烦两件事，第一件事是别人要他给自己的代码写文档，第二件呢？是别人的程序没有留下文档。","当年刚学打篮球的时候，疯狂地迷恋上了乔丹，然后迷恋上了NIKE，更熟记了NIKE的那句广告语：JUST DO IT。 然后…我从此进入了IT行业。。。", "你们用盗版的时候有想过做出这款软件的程序员吗？！他们该如何养家糊口？哈哈哈，别逗了，程序员哪有家要养啊！", "某程序员退休后决定练习书法，于是重金购买文房四宝。一日，饭后突生雅兴，一番研墨拟纸，并点上上好檀香。定神片刻，泼墨挥毫，郑重地写下一行字：hello world！ ",
				"你们饭店需要客户端吗？不忙的时候都是小二端，只有忙的时候才需要客户端。", "从前，有一个程序员，他得到了一盏神灯 。灯神答应实现他一个愿望。然后他向神灯许愿， 希望在有生之年能写一个好项目。后来他得到了永生。", "我是一个苦b的程序员，今晚加班到快通宵了，困得快睁不开眼了，女上司很关心，问我要不要吃宵夜。我没好气地说，宵夜就算了，能让我睡一觉就行了。女上司红着脸说了句讨厌啊，然后坐在我身边不动，好像距离我很近，搞得我很紧张，难道她发现我的程序出了bug？" };

		int index = (int) (Math.random() * body.length);

		return body[index];

	}

}

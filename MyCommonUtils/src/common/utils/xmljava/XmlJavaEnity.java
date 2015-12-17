package common.utils.xmljava;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author tank
 * @email kaixiong.tan@qq.com
 * @date:2015年12月7日 下午5:39:57
 * @description:
 * @version :0.1
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
// FIELD: JAXB 绑定类中的每个非静态、非瞬态字段将会自动绑定到 XML，除非由 XmlTransient 注释。
// NONE: 所有字段或属性都不能绑定到 XML，除非使用一些 JAXB 注释专门对它们进行注释。
// PROPERTY: JAXB 绑定类中的每个获取方法/设置方法对将会自动绑定到 XML，除非由 XmlTransient 注释。
// PUBLIC_MEMBER:每个公共获取方法/设置方法对和每个公共字段将会自动绑定到 XML，除非由 XmlTransient 注释。 (默认)
public class XmlJavaEnity implements Serializable {
	
	private Long id;
	private String name;
	private List<String> phones;
	

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

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public static void main(String[] args) {
		try {
			JAXBContext context = JAXBContext.newInstance(XmlJavaEnity.class);

			Marshaller marshaller = context.createMarshaller();
			Unmarshaller unmarshaller = context.createUnmarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			XmlJavaEnity entity=new XmlJavaEnity();
//			entity.setId(12L);
//			entity.setName("test");
//			
//			List<String> list=new ArrayList<String>();
//			list.add("aaa");
//			list.add("bbb");
//			
//			entity.setPhones(list);
			
			marshaller.marshal(entity, System.out);
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

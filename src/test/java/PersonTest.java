import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by yfxuxiaojun on 2016/9/21.
 */
public class PersonTest {
    @Test(description = "通过内省读属性")
    public void test1() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor :
                propertyDescriptors) {
            System.out.println(propertyDescriptor.getName());
        }
    }

    @Test(description = "通过属性描述器读写属性")
    public void test2() throws Exception {
        Person p = new Person();
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("age", Person.class);
        Method method = propertyDescriptor.getWriteMethod();
        method.invoke(p, 30);

        method = propertyDescriptor.getReadMethod();
        System.out.println(method.invoke(p, null));

    }

    @Test(description = "通过BeanUtils设置bean的属性")
    public void test3() throws Exception {
        Person p = new Person();
        BeanUtils.setProperty(p,"name","许小军");
        System.out.println(p.getName());

    }

    @Test(description = "通过BeanUtils把表单map填充Bean的属性，填充非基本类型的属性，需要注册转换器")
    public void test4() throws Exception {
        Person p = new Person();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "张三");
        map.put("age", "30");
        map.put("birthday", "1980-10-15");


        ConvertUtils.register(new DateLocaleConverter(), Date.class);
        BeanUtils.populate(p, map);

        System.out.println(p);
    }

    @Test(description = "注册转换器，转换引用类型数据")
    public void test5() throws Exception {
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (!(value instanceof String)) throw new RuntimeException("传入的不是字符串");
                if (((String) value).trim().equals("")) throw new RuntimeException("传入的字符串为空");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = df.parse((String) value);
                    return date;
                } catch (ParseException e) {
                    throw new RuntimeException("格式不符合，转型失败");
                }

            }
        },Date.class);

        Person p = new Person();
        BeanUtils.setProperty(p,"name","许小军");
        BeanUtils.setProperty(p,"birthday","1990-10-5");
        System.out.println(p);

    }

    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("*******************************开始测试*****************************************");

    }
}
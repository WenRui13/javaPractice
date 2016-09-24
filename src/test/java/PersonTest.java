import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        BeanUtils.setProperty(p, "name", "许小军");
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
                if (value == null) {
                    return null;
                }
                if (!(value instanceof String)) {
                    throw new ConversionException("传入的不是字符串");
                }
                if (((String) value).trim().equals("")) {
                    return null;
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = df.parse((String) value);
                    return date;
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        }, Date.class);

        Person p = new Person();
        BeanUtils.setProperty(p, "name", "许小军");
        BeanUtils.setProperty(p, "birthday", "1999-9-9");
        System.out.println(p);

    }

    @Test(description = "测试Map.entrySet()")
    public void test6() throws Exception {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");

        Set<Map.Entry<Integer, String>> set = map.entrySet();
        Iterator<Map.Entry<Integer, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

    }


    @Test(description = "测试自定义泛型方法")
    public void test7() throws Exception {
        String[] str = new String[]{"1111", "2222", "3333"};
        reverse(str);

        System.out.println(Arrays.toString(str));

    }

    public <T> void reverse(T arr[]) {
        int start = 0;
        int end = arr.length - 1;


        while (true) {

            if (start >= end) {
                break;
            }
            T temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;

            start++;
            end--;
        }

    }

    @Test(description = "文件读写测试")
    public void test8() throws Exception {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");

        File file = new File("./map.txt");
        writeMapToFile(map, file);
        printFile(file);
    }

    @Test(description = "测试")
    public void test9() throws Exception {
        System.out.println(String.valueOf(22));
    }

    @Test(description = "test10")
    public void test10() throws Exception {

    }

    private void printFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line != null) {
            System.out.println(line);
            line = br.readLine();
        }

        br.close();

    }

    private void writeMapToFile(HashMap<Integer, String> map, File file) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        Set<Map.Entry<Integer, String>> entrySet = map.entrySet();

        Iterator<Map.Entry<Integer, String>> iterator = entrySet.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();

            bw.write(entry.getKey() + "=" + entry.getValue());
            bw.newLine();
        }

        bw.close();
    }

    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("*******************************开始测试*****************************************");

    }
}
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
    @Test(description = "ͨ����ʡ������")
    public void test1() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor :
                propertyDescriptors) {
            System.out.println(propertyDescriptor.getName());
        }
    }

    @Test(description = "ͨ��������������д����")
    public void test2() throws Exception {
        Person p = new Person();
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("age", Person.class);
        Method method = propertyDescriptor.getWriteMethod();
        method.invoke(p, 30);

        method = propertyDescriptor.getReadMethod();
        System.out.println(method.invoke(p, null));

    }

    @Test(description = "ͨ��BeanUtils����bean������")
    public void test3() throws Exception {
        Person p = new Person();
        BeanUtils.setProperty(p, "name", "��С��");
        System.out.println(p.getName());

    }

    @Test(description = "ͨ��BeanUtils�ѱ�map���Bean�����ԣ����ǻ������͵����ԣ���Ҫע��ת����")
    public void test4() throws Exception {
        Person p = new Person();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "����");
        map.put("age", "30");
        map.put("birthday", "1980-10-15");


        ConvertUtils.register(new DateLocaleConverter(), Date.class);
        BeanUtils.populate(p, map);

        System.out.println(p);
    }

    @Test(description = "ע��ת������ת��������������")
    public void test5() throws Exception {
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                if (value == null) {
                    return null;
                }
                if (!(value instanceof String)) {
                    throw new ConversionException("����Ĳ����ַ���");
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
        BeanUtils.setProperty(p, "name", "��С��");
        BeanUtils.setProperty(p, "birthday", "1999-9-9");
        System.out.println(p);

    }

    @Test(description = "����Map.entrySet()")
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


    @Test(description = "�����Զ��巺�ͷ���")
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

    @Test(description = "�ļ���д����")
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

    @Test(description = "����")
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
        System.out.println("*******************************��ʼ����*****************************************");

    }
}
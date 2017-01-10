import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sun.java2d.pipe.ShapeSpanIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

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
                    return df.parse((String) value);
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
        for (Map.Entry<Integer, String> entry : set) {
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
        BeanUtils.populate(new Person(), new HashMap());
    }

    @Test
    public void test11() throws Exception {
        System.out.println("第十一个测试用例");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    @Test(description = "测试枚举类型的values返回枚举数组")
    public void test12() throws Exception {
        for (ColorEnum color : ColorEnum.values()) {
            System.out.println(color);
        }

        System.out.println(Enum.valueOf(ColorEnum.class, "GREEN"));
    }

    @Test(description = "测试枚举工具类EnumSet的使用")
    public void test13() throws Exception {
        System.out.println("====allOf(Class<E> elementType)=====");
        EnumSet<ColorEnum> set = EnumSet.allOf(ColorEnum.class);
        pritSetElements(set);

        System.out.println("====noneOf(Class<E> elementType)=====");
        EnumSet<ColorEnum> set1 = EnumSet.noneOf(ColorEnum.class);
        set1.addAll(set);
        pritSetElements(set1);

        System.out.println("===clone()======");
        EnumSet<ColorEnum> clone = set.clone();
        pritSetElements(clone);

        System.out.println("====complementOf(EnumSet<E> s)=====");
        EnumSet<ColorEnum> coldColor = EnumSet.of(ColorEnum.BLUE, ColorEnum.GREEN);
        EnumSet<ColorEnum> noColdColor = EnumSet.complementOf(coldColor);
        pritSetElements(noColdColor);

        System.out.println("====range(E from, E to)=====");
        EnumSet<ColorEnum> range = EnumSet.range(ColorEnum.RED, ColorEnum.BLUE);
        pritSetElements(range);

        System.out.println("====copyOf(EnumSet<E> s)=====");
        EnumSet<ColorEnum> copyOf = EnumSet.copyOf(set);
        pritSetElements(copyOf);

        System.out.println("====copyOf(Collection<E> c)=====");
        List<ColorEnum> list = Arrays.asList(ColorEnum.values());
        EnumSet<ColorEnum> copyOf1 = EnumSet.copyOf(list);
        pritSetElements(copyOf1);

    }


    private <T extends Collection> void pritSetElements(T t) {
        for (Object aT : t) {
            System.out.println(aT);
        }
    }

    @Test(description = "测试枚举工具类EnumMap")
    public void test14() throws Exception {
        EnumMap<ColorEnum, String> enumMap = new EnumMap<ColorEnum, String>(ColorEnum.class);
        enumMap.put(ColorEnum.RED, "红色");
        enumMap.put(ColorEnum.BLUE, "蓝色");
        enumMap.put(ColorEnum.GREEN, "绿色");

        for (ColorEnum color : ColorEnum.values()) {
            System.out.println(enumMap.get(color));
        }

    }

    @Test
    public void test15() throws Exception {
        System.out.println(ColorEnum.RED.getDesc());
        System.out.println(ColorEnum.RED.getInfo());
    }

    @Test(description = "序列化与反序列化对象")
    public void test16() throws Exception {
        System.out.println("创建一个对象");
        Person person = new Person();
        person.setAge(20);
        person.setName("许小军");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        person.setBirthday(sf.parse("1985-10-15"));

        System.out.println("序列化person对象到数组");
        byte[] bytes = SerializationUtils.serialize(person);

        System.out.println("保存到本地文件");
        FileOutputStream outputStream = new FileOutputStream(new File("./hello.txt").getAbsoluteFile());

        SerializationUtils.serialize(person, outputStream);
    }

    @Test
    public void test17() throws Exception {
        FileInputStream inputStream = new FileInputStream(new File("./hello.txt").getAbsoluteFile());
        Person person = (Person) SerializationUtils.deserialize(inputStream);
        System.out.println(person);
    }

    @Test
    public void test18() throws Exception {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);
        String format1 = format.format(new Date());
        System.out.println(format1);

    }


    @Test
    public void test19() throws Exception {
        double aDouble = RandomUtils.nextDouble() * 100;
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        System.out.println(numberFormat.format(aDouble));

    }

    @Test
    public void test20() throws Exception {
        EnumMap<ColorEnum, String> enumMap = new EnumMap<ColorEnum, String>(ColorEnum.class);
        EnumSet<ColorEnum> enumSet = EnumSet.allOf(ColorEnum.class);
        ColorEnum[] values = ColorEnum.values();
        for (ColorEnum colorEnum : values) {
            enumMap.put(colorEnum, colorEnum.getInfo());
        }
        for (ColorEnum colorEnum : enumSet) {
            enumMap.put(colorEnum, colorEnum.getDesc());
        }

        System.out.println(enumMap.containsKey(ColorEnum.BLUE));
        System.out.println(enumMap.containsValue("红色"));
        System.out.println(enumMap);

    }

    @Test
    public void test21() throws Exception {
        Person person = new Person();
        person.setName("许小军");
        person.setAge(20);
        person.setBirthday(new Date());

        Person person1 = person.clone();
        System.out.println(person1);

    }

    @Test
    public void test22() throws Exception {
        // start the proxy
        BrowserMobProxyServer server = new BrowserMobProxyServer();

        server.start();

        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(server);

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        // start the browser up
        WebDriver driver = new FirefoxDriver(capabilities);
        driver.manage().window().maximize();
        // set test url and cookie which is copied from browser.
        String testUrl = "http://prom.shop.jd.net/promotion/info/info2_queryActivitys.action?__vender_id=58725&pageView.evtType=1";
        final String cookieValue = "__jda=248191580.14828136823411804450879.1482813682.1482984178.1483087292.5; __jdv=248191580%7Cdirect" +
                "%7C-%7Cnone%7C-%7C1483087292009; __jdu=ed091c6a-a95a-4871-84a0-8788fd142e68; _pst=test_fbp_smd2; pin" +
                "=test_fbp_smd2; unick=test_fbp_smd2; _tp=YMDNxagB2%2BBXSTg%2BeRaWGA%3D%3D; TrackID=1MlejGWPUo091ow2kAA6AEK13AeMBcKJNMTp3dMglURg4xejUVpNCOk6U4LQ2Pkp_-rtMWN8kNig1DccW0nNv3cHnB0QtcVnAT77SzS5X7bQ" +
                "; pinId=7_QQtS9Lt59dSs1P4UbBrw; _jrda=3; ceshi3.com=4B4C76F75C02AD1D5E02D0222AA88801F9D29175657D28FD" +
                "99C9513299E68CEDE8550FF83D3EEA3B4F0B6B2DBB6A80D710C38D68C2DE2730BFDB277B7BFEE30B44F7491E093F5529B31F" +
                "3A65385668EED789F822D9FFC428FA7CB49DEAD89AF4A9A4290ED087F74BBCE7433A54907FAE75E615F5D971D8A88AF34FF405371CAA30E6468958F27C5117E1291244B10996" +
                "; logining=1; _vender_=XIS4VVGW4FROFQ65XGJM4LR3RSRO4XRPUXZD7O7OHWXJDNRA7JTNLKGYJKURVD3CKNU3PTHZUZRA3" +
                "ERLTHRKL6YUQS2QMYERKDVREKJXXJBWKPKT4SLFZEHOLHETWBDTO2EU5D4U2RCWBIKND7PWTVEXBTL6JE6WIZYH5A6SJWBMYS3SX" +
                "2F2VWNQQ63R2TKSY4AZ4MENLFM4Y6ALKKKMQUAM5BNCXO3O5PLR5BVDKRHUXR2JUDYZ3J2CYB6TTITDGVYDRQZO22USJDLXGVTYZ" +
                "KENYV4HP2CNOTBYZ2ZK2EPY6CYZQOVODDYM34HCDGZV4YTIMQQ2JDOXHIU33AEODJYSAH7NRQZTWQXF3QN6UJKHHFCUHK4HX24R2" +
                "EUSNHUCBX3XF6YONJANORE3KEAW6BEX2F3CFU5FH533PBA"; //登录后获取请求中cookie的那长串内容
        server.addRequestFilter(new RequestFilter() {
            @Override
            public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
                request.headers().add("Cookie", cookieValue);
                return null;
            }
        });
        driver.get(testUrl);
        Thread.sleep(10000);
        driver.quit();
        server.stop();
    }


    @Test
    public void testBmp_1() throws Exception {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start();
        int port = proxy.getPort();

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        WebDriver driver = new FirefoxDriver(capabilities);
        System.out.println("Port started:" + port);

        String testUrl = "http://prom.shop.jd.com/promotion/info/info2_queryActivitys.action";
        final String cookieValue = "__jdu=1484047281712572965388; erp1.jd.com=18607A30C86DFF8DDDD476F4015C717501360402B91419F75D6444163F" +
                "B33530491CF0D42EE10DB78732D48224E16CF2008228C7C629B2A97874CA346520AE07A5C0044E41C396944401AC26DC56B382C10E900E5E8CE6B168F4971416FD29F2" +
                "; __jda=122270672.1484047281712572965388.1484047282.1484047282.1484047282.1; __jdb=122270672.1.1484047281712572965388" +
                "|1.1484047282; __jdc=122270672; __jdv=122270672|direct|-|none|-|1484047281758; 3AB9D23F7A4B3C9B=LNBPXE537WZNCJU3YGU5BL5KKQSC4NIVYTR4TMXRYCQFL5WMMHODKUNOPEWVJALEVWG5FANOSGQ62ZXZ7F4O5LRKD4" +
                "; _jrda=1; _jrdb=1484047282202; TrackID=1tx3s9TA30mgRrY9sSmyKHforGyR87ZS9JcsV2jzVOLG0_H4H24gUhJ4A_p89l6FT6ZZhER3fOzYDEa3UZiAWrQV6ybE555E2Hu2vCm6KXOI" +
                "; pinId=pVvgI0wl9Kj30k3e9u7OvQ; pin=test_pop_sop01; thor=8D773357DE4B1262E9D9589251555952402FA3355B2" +
                "5D0A45EE63D64D7C2BB817137DDA454E90819FDB0725373104C0BA62C894067FCA235256C5EBDF3E0F5D42C933E5AB41098A" +
                "7CC39A9B04B645C657ABDB134DBFD608DE2CE68F93151EBEFB93DE712A56DBC9825A691B08F97CCF55FAD595FFDB365C02D135F32033AEC56B73151C916665F049E9D0B095E6EFBBC" +
                "; _tp=eC2QtosdwG106sIRKR64AQ%3D%3D; logining=1; _pst=test_pop_sop01; ceshi3.com=2002fCLT34SOTIuS5OlJ55F-NDdkgFxv7CWUxheQ350" +
                "; _vender_=VOGSX64M3JVSAF7GGR7SK77P2BYK562ZVZJIHKND2MLR4QMHDNAQLPMTKJILMAPLW2E723QH2UEKOUYXTBVMMNAD4" +
                "TIKZ3YLDQ7B5HQK4PMO6EEQLM2YAXH35DEYQSMDKEG7LYPBDGQ4OTRAYDG2RSQQXOHR5M3UPO3ZTUKOEDAM3KGKCC54LKCZWOL6T" +
                "SZAZSJIPTDBKCUARUW3N22UF32DLFLEIQGVO2W55J6AKPPBT227XA5NO4S3NFVIUUUADGOYH56T72MTMNTP5AAMDRLULPTG62GGT" +
                "55FZ4EXAHVVHY2HYTUFB7U4PQL7NGJFCJ6F2RFPQZMU6ZNR7LLUY3SOG2AERCQH4RXS2MWOBJP7LFD5OJONF4FHVK6LHPYAJQIRCQ6CBPOXLMNEN6QF3WFILXLOXJFQWTFRZEK3FCL3BLRV4UO67YCXPG2FAGLX" +
                "; _lvtc_=W2E723QH2UEKOCDMUMBL2E24QU";
        proxy.addRequestFilter(new RequestFilter() {
            @Override
            public HttpResponse filterRequest(HttpRequest httpRequest, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
                httpRequest.headers().add("Cookie", cookieValue);
                return null;
            }
        });

        driver.get(testUrl);
        Thread.sleep(10000);
        driver.quit();
        proxy.stop();
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

        for (Map.Entry<Integer, String> entry : entrySet) {
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
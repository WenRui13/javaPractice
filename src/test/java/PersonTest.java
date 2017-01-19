import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.proxy.dns.AdvancedHostResolver;
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
import org.testng.Assert;
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
import java.net.InetAddress;
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
        final String cookieValue = "_jrda=2; _tp=YMDNxagB2%2BBXSTg%2BeRaWGA%3D%3D; unick=test_fbp_smd2; _pst=test_fbp_smd2; TrackID=1qTn" +
                "dI7r_ZpfUCzTZo0fwdQrHzhDrQm7FShIZWkEUtiZJOxT2fsCgsznyHVT0juXLm7H9-UBA3fZxkhtQ4wBeqaMvy4qDdGmspQQksloAnGg" +
                "; pinId=7_QQtS9Lt59dSs1P4UbBrw; pin=test_fbp_smd2; _jrdb=1484190424688; ceshi3.com=8C216B6E82AB31C21" +
                "2D4126475108708BA102028E51039EFACF33E17DB4636CD754F5891939D96236966C72757F88E4E8C55507BC90D99B42B593" +
                "2CF6036278901E6722F96DCF7DEAEA7D2DDBC142A56C0AF779D26F527982A8005DC1A7C01180B47C6F8651799DF6D4E25D8F7E163A9C0A951102EADCEDF9FC65007EC2B92AA4F901BA37ABE1F1EE15C7858A1925CF6" +
                "; logining=1"; //登录后获取请求中cookie的那长串内容
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
        final String cookieValue = "__jdu=1484047281712572965388; __jda=122270672.1484047281712572965388.1484047282.1484047282.1484190337" +
                ".2; __jdv=122270672|direct|-|none|-|1484047281758; _jrda=2; TrackID=1Bqqkmh5LfxKNX1TRwv94nwaqksODnV_koLOSWETbNUO7kDjV1-8tg22Flb9zIwLwPQn3-cfalCtXy9s25L1wfBc-eQHmH4LnCoZUYJmjHyI" +
                "; pinId=pVvgI0wl9Kj30k3e9u7OvQ; pin=test_pop_sop01; _tp=eC2QtosdwG106sIRKR64AQ%3D%3D; _pst=test_pop_sop01" +
                "; __jdb=122270672.1.1484047281712572965388|2.1484190337; __jdc=122270672; 3AB9D23F7A4B3C9B=LNBPXE537WZNCJU3YGU5BL5KKQSC4NIVYTR4TMXRYCQFL5WMMHODKUNOPEWVJALEVWG5FANOSGQ62ZXZ7F4O5LRKD4" +
                "; _jrdb=1484190337583; thor=80F201C8ADFBDD446D096E2D679D3D23A44B481C9C4517579FA9B4FC60E1B050E83B1BCE" +
                "B8D28A1C78C5F2D7B5B54411CB0F4E64DF5669E2A82631376C35218424351AA98515E860B4057AD31F8A51572FCB0D5D8E07" +
                "F1CF7A68094B09EE5DFB4D0C544042CD3B70B1A5732F94B8A11BC0B6A66DD33A44CCCA8B0524465F48DAD6A9F19905B0FC2DC9AE8A75789180BB" +
                "; logining=1; ceshi3.com=2002fCLT34SOTIuS5OlJ55F-NDdkgFxv7CWUxheQ350; _vender_=VOGSX64M3JVSAF7GGR7SK" +
                "77P2BYK562ZVZJIHKND2MLR4QMHDNAQLPMTKJILMAPLAYM6FSL2PKO6LBOIKQGKALS2TPIKZ3YLDQ7B5HQK4PMO6EEQLM2YAXH35" +
                "DEYQSMDKEG7LYPBDGQ4OTRAYDG2RSQQXOHR5M3UPO3ZTUKOEDAM3KGKCC54LKCZWOL6TSZAZSJIPTDBKCUARUW3N22UF32DLFLEI" +
                "QGVO2W55J6AKPPBT227XA5NO4S3NFVIUUUADGOYH56T72MTMNTP5AAMDRLULPTG62GGT55FZ4EXAHVVHY2HYTUFB7U4PQL7NGJFC" +
                "J6F2RFPQZMU6ZNR7LLUY3SOG2AERCQH4RXS2MWOBJP7LFD5OJONF4FHVK6LHPYAJQIRCQ6CBPOXLMNEN6QF3WFILXLOXJFQWTFRZEK3FCL3BLRV4UO67YCXPG2FAGLX" +
                "; _lvtc_=AYM6FSL2PKO6KLDEKCFY6OF46M";
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

    @Test
    public void testBMP_2() throws Exception {
        BrowserMobProxy mobProxy = new BrowserMobProxyServer();
        InetAddress inetAddress = InetAddress.getByAddress("prom.shop.jd.com", new byte[]{(byte) 211, (byte) 152, 122, (byte) 10});
//        mobProxy.start(0,InetAddress.getLocalHost(),inetAddress);
        mobProxy.start(0);
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(mobProxy,inetAddress);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        WebDriver driver = new FirefoxDriver(capabilities);
        String testUrl = "http://prom.shop.jd.com/promotion/info/info_queryPromotions.action?pageView.evtType=1";
        final String cookieValue = "__jdu=1484047281712572965388; __jda=122270672.1484047281712572965388.1484047282.1484486731.1484531657" +
                ".9; __jdv=122270672%7Cdirect%7C-%7Cnone%7C-%7C1484273421182; TrackID=1qLB4cWVaiPgQYZKJ5Y166iDq6A5lgaC5ZXfWwKDP2FRkVA--Hek5MPCiOpBlQ76Uy1DEIgKFwVR2JRrJ-vbQp2nYEzudp2ZG2i2VcTGKneA" +
                "; pin=test_pop_sop01; _tp=eC2QtosdwG106sIRKR64AQ%3D%3D; _pst=test_pop_sop01; user-key=7c758b58-f743-4e53-abc5-61b756538980" +
                "; cn=106; ipLoc-djd=1-72-2819-0; ipLocation=%u5317%u4EAC; erp1.jd.com=C7989CC2F50FBA31F8239C0800467D" +
                "651EA3B10EF5BC02613A1C3B1EB31BCAB7682DE4D7478C65D0F5708439163568BB6F306D303679D5ADD49CB9E512C3CAAE41651E0EC62ED9F9881FF1833CC51DD90109F9619176D49E00154846372D6187" +
                "; sso.jd.com=d7db8cd7abf040d4aeb9952750b58bab; __jdc=122270672; _jrda=2; pinId=pVvgI0wl9Kj30k3e9u7OvQ" +
                "; logining=1; ceshi3.com=2002fCLT34SOTIuS5OlJ55F-NDdkgFxv7CWUxheQ350; 3AB9D23F7A4B3C9B=LNBPXE537WZNCJU3YGU5BL5KKQSC4NIVYTR4TMXRYCQFL5WMMHODKUNOPEWVJALEVWG5FANOSGQ62ZXZ7F4O5LRKD4" +
                "; thor=F5C05C86E1B54BAAC81642064B7F7946060CB6467028A0C8DC31EA9F6931F3E6F6000B045DEC4ADA2E93ADF683184" +
                "88326075654B6C8169FF3249D31BE6856807F564B2777D962308A6DE0747004EC332941489F0FA9B791DAA818354CD0BAE523E5610770089E870A6704FE6BA9179A9A31F6230E080A25F43D8569C357F6483DEA469433827961A4B73274A728FBF8" +
                "; _vender_=VOGSX64M3JVSAF7GGR7SK77P2BYK562ZVZJIHKND2MLR4QMHDNAQLPMTKJILMAPLETXH2GXHRYIHJWRINA5TLVMYA" +
                "LIKZ3YLDQ7B5HQK4PMO6EEQLM2YAXH35DEYQSMDKEG7LYPBDGQ4OTRAYDG2RSQQXOHR5M3UPO3ZTUKOEDAM3KGKCC54LKCZWOL6T" +
                "SZAZSJIPTDBKCUARUW3N22UF32DLFLEIQGVO2W55J6AKPPBT227XA5NO4S3NFVIUUUADGOYH56T72MTMNTP5AAMDRLULPTG62GGT" +
                "55FZ4EXAHVVHY2HYTUFB7U4PQL7NGJFCJ6F2RFPQZMU6ZNR7LLUY3SOG2AERCQH4RXS2MWOBJP7LFD5OJOMJCCTEB4AAXKFPRHO5I3W2AFG4AEPNG4Y3ZJBFSHGN63AE35UYHHDVSUMG7GTANVCCXYVITMSWBH2QBEIUB7EN4WTEJDTEXNVF7FFQU" +
                "; _lvtc_=DSMIEKUIH52MH3VRGQWI3ZJ2HA";

        mobProxy.addRequestFilter(new RequestFilter() {
            @Override
            public HttpResponse filterRequest(HttpRequest httpRequest, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
                httpRequest.headers().add("Cookie", cookieValue);
                return null;
            }
        });

        driver.get(testUrl);

        Thread.sleep(10000);
        Assert.assertTrue(driver.getTitle().contains("已创建的单品促销"));

        System.out.println(mobProxy.getServerBindAddress().getHostName());
        driver.quit();
        mobProxy.stop();
    }

    @Test
    public void testBmp_3() throws Exception {
        BrowserMobProxy mobProxy = new BrowserMobProxyServer();
        mobProxy.start(0, null, InetAddress.getByName("www.yahoo.com"));
        AdvancedHostResolver hostNameResolver = mobProxy.getHostNameResolver();
        Collection<InetAddress> inetAddresses = hostNameResolver.resolve(mobProxy.getServerBindAddress().getHostName());
        Assert.assertNotNull(inetAddresses);
        Assert.assertFalse(inetAddresses.isEmpty());
        System.out.println(inetAddresses);
        System.out.println(mobProxy.getServerBindAddress().getHostAddress());

    }


    @Test
    public void testBmp_4() throws Exception {
        InetAddress[] allByName = InetAddress.getAllByName("www.baidu.com");
        for (InetAddress inetAddress : allByName) {
            System.out.println(inetAddress.getHostAddress());
        }


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


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpClientUtil {
	public static final String urlString = "http://....?passport=...&password=...";  //先登录保存cookie
	public static final String urlString2 = "http://......";
	public String sessionId = "";
	
	public void doGet(String urlStr) throws IOException{
		String key = "";
		String cookieVal = "";
		URL url = new URL(urlStr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect(); //到此步只是建立与服务器的tcp连接，并未发送http请求
		/** 
		 * 设置cookie
		 */
		if(!sessionId.equals("")){
			connection.setRequestProperty("Cookie", sessionId);
		}
		for(int i=1;(key=connection.getHeaderField(i))!=null;i++){
			cookieVal = connection.getHeaderField(i);
			cookieVal = cookieVal.substring(0,cookieVal.indexOf(";")>-1?cookieVal.indexOf(";"):cookieVal.length()-1);
			sessionId = sessionId + cookieVal + ";";
		}
		
		//直到getInputStream()方法调用请求才真正发送出去
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while((line=br.readLine()) != null){
			sb.append(line);
			sb.append("\n");
		}
		System.out.println(sb.toString());
		br.close();
		connection.disconnect();
	}
	
	public void doPost() throws IOException{
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true); //设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
		connection.setDoInput(true); // 设置连接输入流为true
		connection.setRequestMethod("POST"); // 设置请求方式为post
		connection.setUseCaches(false); // post请求缓存设为false
		connection.setInstanceFollowRedirects(true); //// 设置该HttpURLConnection实例是否自动执行重定向
		// 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
        // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.connect();
		
		// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
		DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
        String parm = "storeId=" + URLEncoder.encode("32", "utf-8"); //URLEncoder.encode()方法  为字符串进行编码           
        dataout.writeBytes(parm); 
        dataout.flush();
        dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)           
        // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder(); // 用来存储响应数据           
        // 循环读取流,若不到结尾处
        while ((line = bf.readLine()) != null) {
            sb.append(bf.readLine());
        }
        bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
        connection.disconnect(); // 销毁连接
        System.out.println(sb.toString()); 
	}
	
	public static void main(String[] args) throws IOException {
		HttpClientUtil hcu = new HttpClientUtil();
		hcu.doGet(urlString);
		hcu.doGet(urlString2);
	}
}
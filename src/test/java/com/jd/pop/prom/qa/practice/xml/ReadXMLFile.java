package com.jd.pop.prom.qa.practice.xml;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * @FunctionDesc
 * @Author bjyfxuxiaojun
 * @CreateDate 2017/1/19
 * @Reviewer kongxiangyun
 * @ReviewDate
 */
public class ReadXMLFile {
    @Test
    public void test1() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File("src/test/resource/staff.xml"));

        document.getDocumentElement().normalize();
        System.out.println("Root element :" + document.getDocumentElement().getNodeName());

        NodeList nodeList = document.getElementsByTagName("staff");

    }
}

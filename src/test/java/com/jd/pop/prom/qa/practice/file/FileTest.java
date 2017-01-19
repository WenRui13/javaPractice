package com.jd.pop.prom.qa.practice.file;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @FunctionDesc
 * @Author bjyfxuxiaojun
 * @CreateDate 2017/1/19
 * @Reviewer kongxiangyun
 * @ReviewDate
 */
public class FileTest {
    @Test
    public void testFilePath_1() throws Exception {
        String fileName = "ccx.txt";
        String workingDirectory = System.getProperty("user.dir");
        String absolutePath = workingDirectory + File.separator + fileName;
//        String absolutePath = workingDirectory + System.getProperty("file.separator") + fileName;
        File file = new File(absolutePath);
        System.out.println(file.getAbsolutePath());
        if (file.createNewFile()) {
            System.out.println(fileName + " is created successfull.");
        } else
            System.out.println(fileName + " is already existed!");


    }

    @Test
    public void testFilePath_2() throws Exception {
        String fileName = "newFile.txt";
        String workingDirectory = System.getProperty("user.dir");
        File file = new File(workingDirectory, fileName);
        System.out.println(file.getAbsolutePath());
        if (file.createNewFile()) {
            System.out.println(fileName + " is created successfull.");
        } else
            System.out.println(fileName + " is already existed!");


    }


    @Test
    public void testFilePermission_1() throws Exception {

        File file = new File(System.getProperty("user.dir"), "shellscript.sh");
        if (file.exists()) {
            System.out.println(file.canExecute());
            System.out.println(file.canWrite());
            System.out.println(file.canRead());
        }
        file.setExecutable(false);
        file.setWritable(false);
        file.setReadable(false);

        if (file.createNewFile()) {
            System.out.println("file is created.");
        } else
            System.out.println("file is exist.");

        System.out.println(file.canExecute());
        System.out.println(file.canWrite());
        System.out.println(file.canRead());


    }

    @Test
    public void testBufferReader_1() throws Exception {
        File file = new File(System.getProperty("user.dir"), "ccx.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            System.out.println(currentLine);
        }

        bufferedReader.close();
    }

    @Test
    public void testBufferWriter_1() throws Exception {
        System.out.println("create a file");
        File file = new File(System.getProperty("user.dir"), "file2write.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        for (int i = 0; i < 10; i++) {
            bufferedWriter.write("test filewrite");
            bufferedWriter.newLine();

        }
        bufferedWriter.flush();
        bufferedWriter.close();

        if (file.delete()) {
            System.out.println("file is deleted.");
        } else
            System.out.println("Delete operation is failed.");


    }


    @Test(description = "带文件过滤器的list方法测试")
    public void testFileNameFilter_1() throws Exception {
        String workDir = System.getProperty("user.dir");
        File[] files = new File(workDir).listFiles(new GenericExtFilter(".txt"));
        if (ArrayUtils.isNotEmpty(files)) {
            for (File file : files) {
                if (file.delete())
                    System.out.println(file.getAbsolutePath()+" is deleted.");
            }

        }
    }


    @Test(description = "创建FilenameFilter匿名内部类，实现过滤并删除指定格式文件")
    public void testFileNameFilter_2() throws Exception {
        String workDir = System.getProperty("user.dir");
        File[] files = new File(workDir).listFiles(new FilenameFilter() {
//            匿名内部类实现accept方法
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".sh");
            }
        });
        if (ArrayUtils.isNotEmpty(files)) {
            for (File file : files) {
                if (file.delete())
                    System.out.println(file.getAbsolutePath()+" is deleted.");
            }

        }
    }

    @BeforeMethod
    public void saveMethodName(Method method) {
        System.out.println(method.getName() + "===============");
    }

    public class GenericExtFilter implements FilenameFilter {
        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File dir, String name) {
            if (name.endsWith(ext)) {
                return true;
            }
            return false;
        }
    }
}

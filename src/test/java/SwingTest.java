import com.sun.deploy.util.ArrayUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Created by yfxuxiaojun on 2017/1/8.
 */
public class SwingTest {
    @Test
    public void testJframe_1() throws Exception {
        JFrame jFrame = new JFrame("这是一个测试窗体");
        Container contentPane = jFrame.getContentPane();
        jFrame.setVisible(true);
        JLabel jLabel = new JLabel("这是一个新的label");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(jLabel);
        contentPane.setBackground(Color.white);
        jFrame.setSize(200,150);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    @Test
    public void testJdialog_1() throws Exception {
        JFrame jFrame = new JFrame("测试窗体");
        jFrame.setBackground(Color.white);
        Container container = jFrame.getContentPane();


        final JDialog jDialog = new JDialog(jFrame, "第一个对话框", true);
        Container contentPane = jDialog.getContentPane();
        contentPane.add(new JLabel("这是一个对话框"));
        jDialog.setBounds(120,120,100,100);


        JButton jButton = new JButton("弹出对话框");
        jButton.setBounds(10,10,100,21);
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jDialog.setVisible(true);
            }
        });
        container.add(jButton);
    }

    @Test
    public void testArray() throws Exception {
        String s = RandomStringUtils.randomAlphanumeric(5);
        System.out.println(s);

    }
}

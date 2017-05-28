import xyz.strikezero.shortcutkeyfinder.config.Config;
import xyz.strikezero.shortcutkeyfinder.file.FileLoader;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by song on 16-5-12.
 */
public class App {
    private JTextField queryField;
    private JButton SearchButton;
    private JComboBox softwareComboBox;
    private JPanel panel;
    private JTextArea textArea;
    private Config config;
    private FileLoader curFile;

    public App() {
        //读取配置文件
        config = new Config();
        //载入数据到下拉框中
        this.softwareComboBox.addItem("请选择需要查询的软件");
        for (String software : config.getSoftwareList()) {
            this.softwareComboBox.addItem(software.split("\\.")[0]);
        }
        //添加下拉框变化事件
        softwareComboBox.addItemListener(new ItemListener() {
                                             public void itemStateChanged(ItemEvent e) {
                                                 //添加判定条件，否则会触发两次
                                                 if (e.getStateChange() == ItemEvent.SELECTED) {
                                                     //得到下拉框中选到的编号
                                                     int index = softwareComboBox.getSelectedIndex() - 1;
                                                     if (index >= 0) {
                                                         //通过编号，得知需要读取的文件路径
                                                         String curFilePath = config.getBasePath() + "/" + config.getSoftwareList().get(index);
                                                         System.out.println("reading file : " + curFilePath);
                                                         //调用FileLoader，读取文件并建立索引
                                                         curFile = new FileLoader(curFilePath);
                                                         //设置文本框，显示出该软件所有的快捷键
                                                         textArea.setText(curFile.toString());
                                                         textArea.setCaretPosition(0);
                                                     }
                                                 }
                                             }
                                         }
        );
        //添加按钮点击事件
        SearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = queryField.getText();
                //调用FileLoader的search函数，找到相应的快捷键，并显示到文本框中
                textArea.setText(curFile.search(query));
                textArea.setCaretPosition(0);
            }
        });
        //将回车事件绑定到搜索按钮上
        queryField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ENTER){
                    SearchButton.doClick();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

package xyz.strikezero.shortcutkeyfinder.file;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import xyz.strikezero.shortcutkeyfinder.index.InvertedIndex;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j on 2016/5/13.
 */
public class FileLoader {
    //列表，每一项存储的是快捷键-相应的功能说明的String[2]的数组
    private List<String[]> keyInfo;
    //文件的倒排索引
    private InvertedIndex index;

    public FileLoader(String path) {
        //初始化
        this.keyInfo = new ArrayList<String[]>();
        index = new InvertedIndex();
        try {
            //用POI读取excel数据
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(path));
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            Iterator<org.apache.poi.ss.usermodel.Row> iter = sheet.rowIterator();
            int docId = 0;
            while (iter.hasNext()) {
                //读取excel文件中的一行
                HSSFRow row = (HSSFRow) iter.next();
                String[] line = new String[2];
                //得到快捷键
                line[0] = row.getCell(0).getStringCellValue();
                //如果是空的，则代表已经读取完毕
                if(line[0].trim().equals("")){
                    break;
                }
                //得到对应的功能说明
                line[1] = row.getCell(1).getStringCellValue();
                //添加到数组中
                this.keyInfo.add(line);
                //添加到倒排索引中
                index.add(line[1], docId++);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(List<String[]> keyInfo) {
        this.keyInfo = keyInfo;
    }

    public String search(String query) {
        //去掉空格
        query = query.replace(" ", "");
        //对于这两种情况，返回所有结果
        if(query.equals("")||query.equals("*")){
            return toString();
        }
        StringBuffer buffer = new StringBuffer();
        //调用InvertedIndex的search函数来得到结果所在的行数，一个列表
        List<Integer> docIds = this.index.search(query);
        //遍历列表，从keyInfo中得到具体的信息，并返回
        for (int i : docIds) {
            buffer.append(keyInfo.get(i)[0]).append("\t").append(keyInfo.get(i)[1]).append("\n");
        }
        return buffer.toString();
    }

    //重写toString方法，将所有快捷键与它对应的功能输出
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        int length = this.keyInfo.size();
        for (int i = 0; i < length; i++) {
            buffer.append(keyInfo.get(i)[0]).append("\t").append(keyInfo.get(i)[1]).append("\n");
        }
        return buffer.toString();
    }
}

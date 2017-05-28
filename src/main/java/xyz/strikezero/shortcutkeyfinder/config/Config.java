package xyz.strikezero.shortcutkeyfinder.config;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 16-5-12.
 */
public class Config {
    private String basePath;
    private List<String> softwareList;

    public Config() {
        // 读取xml配置文件
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setNamespaceAware(true);
            DocumentBuilder builder = null;
            builder = docBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse("config.xml");

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            //通过读取basepath和文件列表，将文件路径添加到list中，等待事件触发
            this.basePath = xpath.evaluate("/list/path", doc);
            this.softwareList = new ArrayList<String>();
            NodeList softwareNodeList = (NodeList) xpath.evaluate("/list/auto/name", doc, XPathConstants.NODESET);
            int listLength = softwareNodeList.getLength();
            for (int i = 0; i < listLength; i++) {
                String cur = softwareNodeList.item(i).getTextContent();
                this.softwareList.add(cur);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    public String getBasePath() {
        return basePath;
    }

    public List<String> getSoftwareList() {
        return softwareList;
    }
}

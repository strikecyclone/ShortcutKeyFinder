package xyz.strikezero.shortcutkeyfinder.participle;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 16-5-12.
 */
public class Participle {
    private static JiebaSegmenter segmenter = new JiebaSegmenter();

    //建立索引，分词粒度较细
    public static List<String> index(String s) {
        return part(s, JiebaSegmenter.SegMode.INDEX);
    }

    //搜索，分词粒度较粗
    public static List<String> search(String s) {
        return part(s, JiebaSegmenter.SegMode.SEARCH);
    }

    //调用jieba分词，将结果存储到列表中返回
    private static List<String> part(String s, JiebaSegmenter.SegMode mode) {
        List<String> list = new ArrayList<String>();
        List<SegToken> tokens = segmenter.process(s, mode);
        int length = tokens.size();
        for (int i = 0; i < length; i++) {
            list.add(tokens.get(i).word);
        }
        return list;
    }
}

package xyz.strikezero.shortcutkeyfinder.index;

import java.util.List;

/**
 * Created by j on 2016/5/13.
 */
public class InvertedIndexEntry {
    //文档id，这里用来代表该单词的倒排索引在列表中的位置
    private int docId;
    //单词出现的次数
    private int tf;
    //这个词出现的位置
    private List<Integer> pos;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    public List<Integer> getPos() {
        return pos;
    }

    public void setPos(List<Integer> pos) {
        this.pos = pos;
    }
}

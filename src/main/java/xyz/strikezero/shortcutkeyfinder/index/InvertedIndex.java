package xyz.strikezero.shortcutkeyfinder.index;

import xyz.strikezero.shortcutkeyfinder.participle.Participle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by j on 2016/5/13.
 */
public class InvertedIndex {
    //字典<单词，编号>
    private Map<String, Integer> dict;
    //列表<>
    private List<InvertedIndexEntry> invertedList;

    public InvertedIndex() {
        this.dict = new HashMap<String, Integer>();
        this.invertedList = new ArrayList<InvertedIndexEntry>();
    }

    public void add(String s, int pos) {
        List<String> list = Participle.index(s);
        for (int i = 0; i < list.size(); i++) {
            if (dict.containsKey(list.get(i))) {
                //通过分好的词，从字典中得到数组中的位置，拿到这个词的倒排索引
                InvertedIndexEntry entry = invertedList.get(dict.get(list.get(i)));
                //词出现的次数+1
                int tf = entry.getTf() + 1;
                //添加相应的位置
                entry.getPos().add(pos);
                entry.setTf(tf);
            } else {
                //没有在字典中找到这个词，所以为这个词新建倒排索引
                InvertedIndexEntry entry = new InvertedIndexEntry();
                //词第一次出现
                entry.setTf(1);
                //新建位置列表，并添加当前位置
                List<Integer> posList = new ArrayList<Integer>();
                posList.add(pos);
                entry.setPos(posList);
                entry.setDocId(this.invertedList.size());
                //将当前词，以及它相应的位置，添加到字典中
                this.dict.put(list.get(i), this.invertedList.size());
                //添加到倒排索引列表中
                this.invertedList.add(entry);
            }
        }
    }

    public List<Integer> search(String q) {
        //用来存放结果
        List<Integer> list = new ArrayList<Integer>();
        //对查询语句进行分词
        List<String> queries = Participle.search(q);
        /**
         * 在倒排索引中搜索，找到这些词都出现了功能所对应的快捷键
         * 首先，对于搜索中分出来的第一个词，将它所在的行数存入哈希表中，并将value置0，
         * 从第二个词开始，将它的所在行数逐个在哈希表中查找，如果找到，就将这一行的value+1，
         * 最后将哈希表中的所有元素遍历一遍，将value=分词个数，即每个词都出现了的行，添加到列表中
         */
        Map<Integer, Integer> docs = new HashMap<Integer, Integer>();
        for (int i = 0; i < queries.size(); i++) {
            if (dict.containsKey(queries.get(i))) {
                //得到倒排索引列表中当前词的下标
                int invertedListIndex = this.dict.get(queries.get(i));
                //得到倒排索引中这个词出现的位置信息
                List<Integer> pos = this.invertedList.get(invertedListIndex).getPos();
                //遍历位置
                for (int j = 0; j < pos.size(); j++) {
                    if (i == 0) {
                        docs.put(pos.get(j), 0);
                    } else {
                        if (docs.containsKey(pos.get(j))) {
                            if (docs.get(pos.get(j)) == i - 1) {
                                docs.put(pos.get(j), i);
                            }
                        }
                    }
                }
            } else {
                System.out.println("没找到" + queries.get(i));
            }
        }
        //找到出现了所有词的行
        for (Map.Entry<Integer, Integer> e : docs.entrySet()) {
            if (e.getValue() == queries.size() - 1) {
                list.add(e.getKey());
            }
        }
        return list;
    }

    public Map<String, Integer> getDict() {
        return dict;
    }

    public void setDict(Map<String, Integer> dict) {
        this.dict = dict;
    }

    public List<InvertedIndexEntry> getInvertedList() {
        return invertedList;
    }

    public void setInvertedList(List<InvertedIndexEntry> invertedList) {
        this.invertedList = invertedList;
    }
}

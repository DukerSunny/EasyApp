package tv.douyu.misc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Wenyi Yang on 6/9/2014.
 */
public class HotWordsUtil {
    private static List<String> getGenreicsWords() {
        ArrayList<String> genricsWords = new ArrayList<>();
        genricsWords.add("66666666666");
        genricsWords.add("色情主播,我报警啦！");
        genricsWords.add("不作死就不会死啊");
        genricsWords.add("2333333333333");
        genricsWords.add("主播,求BGM");
        genricsWords.add("瞬间爆炸");
        genricsWords.add("主播不要逗！");
        genricsWords.add("笑尿了");
        genricsWords.add("哈哈哈啊啊");
        genricsWords.add("喜闻乐见");

        return genricsWords;
    }

    private static List<String> getHearthStoneWords() {
        ArrayList<String> words = new ArrayList<>(getGenreicsWords());
        words.add("强行伏笔");
        words.add("解得漂亮");
        words.add("110");
        words.add("呼啦！");
        words.add("不贪怎么赢?");
        words.add("不要怂，就是干！");
        words.add("卖血吊打!");
        return words;
    }

    public static List<String> getHotWords(int cate_id) {
        switch (cate_id) {
            case 1:
                return getLeagueOfLengendsWords();
            case 2:
                return getHearthStoneWords();
            default:
                return getGenreicsWords();
        }
    }

    private static List<String> getLeagueOfLengendsWords() {
        ArrayList<String> lolWords = new ArrayList<>(getGenreicsWords());
        lolWords.add("先点菜吧");
        lolWords.add("又Q歪了");
        lolWords.add("花式补刀");
        lolWords.add("演得不错");
        lolWords.add("浪的飞起");
        return lolWords;
    }

    private static List<String> getWordsBySize(List<String> words, int size) {
        ArrayList<String> result = new ArrayList<>();
        if (size >= words.size()) {
            size = words.size();
        }

        while (result.size() < size) {
            Random r = new Random();
            int i = r.nextInt(words.size());
            String word = words.get(i);
            if (result.contains(word)) {
                continue;
            }
            result.add(word);
        }
        return result;
    }
}

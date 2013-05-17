package net.moraleboost.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.text.CharacterIterator.DONE;

/**
 * CSVデータを解析/作成するためのユーティリティクラス
 *
 * @author taketa
 *
 */
public class CSVUtil
{
    private static char[] charsToBeEscaped = new char[] {' ', '\t', '\"', ','};

    /**
     * 一行のCSVを解析し、カラムに分割して返す。
     *
     * @param str
     *            解析対象
     * @param max
     *            最大カラム数。strがこれ以上のカラムで構成される場合、
     *            max+1番目以降のカラムは、すべてmax番目のカラムにマージされる。
     * @return カラム配列。strが空文字列の場合は、空の配列が返される。
     */
    public static final String[] tokenize(String str, int max)
    {
        List<String> ret = new ArrayList<String>();
        CharacterIterator iter = new StringCharacterIterator(str);
        StringBuilder token;
        char c = DONE;

        for (c=iter.first(); c!=DONE; c=iter.next()) {
            // トークン先頭の空白を読み飛ばす
            while (c == ' ' || c == '\t') {
                c = iter.next();
            }

            token = new StringBuilder();
            if (c == '"') {
                // クォート文字列
                // 終わりまで読む
                while ((c = iter.next()) != DONE) {
                    if (c == '"') {
                        c = iter.next();
                        // 2つ連続する「"」は、エスケープされた「"」
                        if (c == '"') {
                            // これはエスケープされた「"」
                            token.append(c);
                        } else {
                            // クォート文字列の終わり
                            break;
                        }
                    } else {
                        token.append(c);
                    }
                }
                // ","まで文字列を読み飛ばす
                while (c != DONE && c != ',') {
                    c = iter.next();
                }
            } else {
                // 次の","まで、トークンを読み取る
                while (c != DONE && c != ',') {
                    token.append(c);
                    c = iter.next();
                }
            }

            --max;
            if (max <= 0) {
                // これ以上の文字列は、すべて最後のトークンにマージする
                while (c != DONE) {
                    token.append(c);
                    c = iter.next();
                }
            }

            ret.add(token.toString());
        }

        // 汚いハック: 文字列が","で終わる場合、最後に空白要素を追加する
        if (max > 0 && str.endsWith(",")) {
            ret.add("");
        }

        return ret.toArray(new String[ret.size()]);
    }

    /**
     * CSVの要素内で使用できない文字をエスケープする
     *
     * @param str エスケープする文字列
     * @return エスケープされた文字列
     */
    public static final String escape(String str)
    {
        // 「 」「\t」「"」「,」のいずれかが含まれていれば、ダブルクォーテーションで囲む
        StringBuilder ret = null;
        char c;
        for (int i=0; i<str.length(); ++i) {
            c = str.charAt(i);
            if (ret == null) {
                for (char ec: charsToBeEscaped) {
                    if (c == ec) {
                        ret = new StringBuilder();
                        ret.append('"');
                        ret.append(str.subSequence(0, i));
                        --i;
                        break;
                    }
                }
            } else {
                if (c == '"') {
                    ret.append("\"\"");
                } else {
                    ret.append(c);
                }
            }
        }

        if (ret == null) {
            return str;
        } else {
            ret.append('"');
            return ret.toString();
        }
    }

    /**
     * elementsの要素をカラムとして、一行のCSVデータを作成して返す。
     *
     * @param elements 各項目の値
     * @return 一行分のCSVデータ
     */
    public static final String join(Collection<String> elements)
    {
        StringBuilder b = new StringBuilder();

        boolean first = true;
        for (String e: elements) {
            if (first) {
                first = false;
            } else {
                b.append(",");
            }
            b.append(escape(e));
        }

        return b.toString();
    }

    /**
     * elementsの要素をカラムとして、一行のCSVデータを作成して返す。
     *
     * @param elements 各項目の値
     * @return 一行分のCSVデータ
     */
    public static final String join(String[] elements)
    {
        return join(Arrays.asList(elements));
    }
}

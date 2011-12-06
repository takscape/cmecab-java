package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;

public class JapaneseNormalizationFilter extends TokenFilter
{
    private boolean normFullWidth; // 全角英数字＋記号を半角に変換
    private boolean normHalfWidth; // 半角カナを全角カナに変換
    private boolean normBlank;     // 空白文字をスキップ

    private final CharTermAttribute termAttribute;
    private final PositionIncrementAttribute posIncAttribute;

    private static final int[][] HALFWIDTH_KANA_TABLE = new int[][] {
            new int[] {'。', 0, 0}, new int[] {'「', 0, 0}, new int[] {'」', 0, 0}, new int[] {'、', 0, 0},
            new int[] {'・', 0, 0}, new int[] {'ヲ', 0, 0}, new int[] {'ァ', 0, 0}, new int[] {'ィ', 0, 0},
            new int[] {'ゥ', 0, 0}, new int[] {'ェ', 0, 0}, new int[] {'ォ', 0, 0}, new int[] {'ャ', 0, 0},
            new int[] {'ュ', 0, 0}, new int[] {'ョ', 0, 0}, new int[] {'ッ', 0, 0}, new int[] {'ー', 0, 0},
            new int[] {'ア', 0, 0}, new int[] {'イ', 0, 0}, new int[] {'ウ', 'ヴ', 0}, new int[] {'エ', 0, 0},
            new int[] {'オ', 0, 0}, new int[] {'カ', 'ガ', 0}, new int[] {'キ', 'ギ', 0}, new int[] {'ク', 'グ', 0},
            new int[] {'ケ', 'ゲ', 0}, new int[] {'コ', 'ゴ', 0}, new int[] {'サ', 'ザ', 0}, new int[] {'シ', 'ジ', 0},
            new int[] {'ス', 'ズ', 0}, new int[] {'セ', 'ゼ', 0}, new int[] {'ソ', 'ゾ', 0}, new int[] {'タ', 'ダ', 0},
            new int[] {'チ', 'ヂ', 0}, new int[] {'ツ', 'ヅ', 0}, new int[] {'テ', 'デ', 0}, new int[] {'ト', 'ド', 0},
            new int[] {'ナ', 0, 0}, new int[] {'ニ', 0, 0}, new int[] {'ヌ', 0, 0}, new int[] {'ネ', 0, 0},
            new int[] {'ノ', 0, 0}, new int[] {'ハ', 'バ', 'パ'}, new int[] {'ヒ', 'ビ', 'ピ'}, new int[] {'フ', 'ブ', 'プ'},
            new int[] {'ヘ', 'ベ', 'ペ'}, new int[] {'ホ', 'ボ', 'ポ'}, new int[] {'マ', 0, 0}, new int[] {'ミ', 0, 0},
            new int[] {'ム', 0, 0}, new int[] {'メ', 0, 0}, new int[] {'モ', 0, 0}, new int[] {'ヤ', 0, 0},
            new int[] {'ユ', 0, 0}, new int[] {'ヨ', 0, 0}, new int[] {'ラ', 0, 0}, new int[] {'リ', 0, 0},
            new int[] {'ル', 0, 0}, new int[] {'レ', 0, 0}, new int[] {'ロ', 0, 0}, new int[] {'ワ', 0, 0},
            new int[] {'ン', 0, 0}, new int[] {'゛', 0, 0}, new int[] {'゜', 0, 0}
    };

    public JapaneseNormalizationFilter(
            TokenStream input, boolean normFullWidth, boolean normHalfWidth, boolean normBlank)
    {
        super(input);
        this.normFullWidth = normFullWidth;
        this.normHalfWidth = normHalfWidth;
        this.normBlank = normBlank;
        termAttribute = addAttribute(CharTermAttribute.class);
        posIncAttribute = addAttribute(PositionIncrementAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException
    {
        if (input.incrementToken()) {
            char[] buf = termAttribute.buffer();
            int len = termAttribute.length();
            int c, tblpos;
            int i, j;
            for (i=0, j=0; i<len;) {
                c = buf[i];
                if (normFullWidth && 0xFF01 <= c && c <= 0xFF5D) {
                    // 全角記号、全角数字、全角アルファベット（「～」を除く）
                    buf[j] = (char)(c - 65248); // 半角に変換
                    ++i; ++j;
                } else if (normHalfWidth && 0xFF61 <= c && c <= 0xFF9F) {
                    // 半角カナ
                    tblpos = c - 0xFF61;
                    // 濁音？
                    if (HALFWIDTH_KANA_TABLE[tblpos][1] != 0) {
                        if (i+1 < len && (buf[i+1] == '゛' || buf[i+1] == 'ﾞ')) {
                            // 次の文字が濁点
                            buf[j] = (char)HALFWIDTH_KANA_TABLE[tblpos][1];
                            i += 2; ++j;
                            continue;
                        }
                    }
                    // 半濁音？
                    if (HALFWIDTH_KANA_TABLE[tblpos][2] != 0) {
                        if (i+1 < len && (buf[i+1] == '゜' || buf[i+1] == 'ﾟ')) {
                            // 次の文字が半濁点
                            buf[j] = (char)HALFWIDTH_KANA_TABLE[tblpos][2];
                            i += 2; ++j;
                            continue;
                        }
                    }
                    // 濁音でも半濁音でもない
                    buf[j] = (char)HALFWIDTH_KANA_TABLE[tblpos][0];
                    ++i; ++j;
                } else if (normBlank && Character.isWhitespace(c)) {
                    buf[j] = ' '; // 半角スペースに変換
                    ++i; ++j;
                } else {
                    // 無変換
                    buf[j] = buf[i];
                    ++i; ++j;
                }
            }
            // ここで、jは新しいbufの長さ
            termAttribute.setLength(j);
            return true;
        } else {
            return false;
        }
    }

    public void reset() throws IOException
    {
        super.reset();
        clearAttributes();
    }
}

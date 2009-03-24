/*
 **
 **  Mar. 1, 2008
 **
 **  The author disclaims copyright to this source code.
 **  In place of a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **
 **                                         Stolen from SQLite :-)
 **  Any feedback is welcome.
 **  Kohei TAKETA <k-tak@void.in>
 **
 */
package net.moraleboost.mecab;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import net.moraleboost.io.CharsetUtil;

public class Tagger
{
    static {
        System.loadLibrary("CMeCab");
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println("MeCab version is " + Tagger.version());
        System.out.println();

        String text = args[1];
        System.out.println("Original text: " + text);
        System.out.println();

        System.out.println("Morphemes:");
        Tagger tagger = new Tagger(args[0], "");
        Node node = tagger.parse(text);
        while (node.hasNext()) {
            String surface = node.next();
            String feature = node.feature();
            System.out.println(surface + "\t" + feature);
        }
    }

    private CharsetDecoder decoder = null;
    private CharsetEncoder encoder = null;
    private Node node = null;
    private long handle = 0;

    /**
     * 形態素解析器を構築する。
     * 
     * @param dicCharset
     *            MeCabの辞書の文字コード。WindowsではShift_JIS、UNIX系ではEUC-JPであることが多いであろう。
     * @param arg
     *            MeCabに与える引数。MeCab::createTagger(const char*)の引数として与えられる。
     * @throws MeCabException
     *             ネイティブライブラリの内部エラー
     */
    public Tagger(String dicCharset, String arg) throws MeCabException
    {
        decoder = CharsetUtil.createDecoder(dicCharset,
                CodingErrorAction.IGNORE, CodingErrorAction.IGNORE);
        encoder = CharsetUtil.createEncoder(dicCharset,
                CodingErrorAction.IGNORE, CodingErrorAction.IGNORE);

        handle = _create(arg.getBytes());
        if (handle == 0) {
            throw new MeCabException("Failed to create a tagger.");
        }
    }

    protected void finalize()
    {
        close();
    }

    /**
     * 形態素解析器を破棄し、リソースを解放する。parse()の呼び出しにより返された既存のNodeは無効化される。
     */
    public void close()
    {
        if (node != null) {
            node.close();
            node = null;
        }

        if (handle != 0) {
            _destroy(handle);
            handle = 0;
        }
    }

    /**
     * 形態素解析を行う。
     * 
     * @param text
     *            解析対象文字列
     * @return Nodeオブジェクト
     * @throws CharacterCodingException
     *             textをバイト列にエンコードできなかった
     * @throws MeCabException
     *             ネイティブライブラリの内部エラー
     */
    public Node parse(CharSequence text) throws CharacterCodingException,
            MeCabException
    {
        // 前の解析結果のノードを無効化する
        if (node != null) {
            node.close();
            node = null;
        }

        // 新しいテキストを解析
        long nodehdl = _parse(handle, CharsetUtil.encode(encoder, text, false));
        if (nodehdl == 0) {
            throw new MeCabException("Failed to parse text.");
        }
        node = new Node(nodehdl, decoder, encoder);

        return node;
    }

    /**
     * バージョン文字列を取得する
     * 
     * @return バージョン文字列
     */
    public static String version()
    {
        return new String(_version());
    }

    private static native long _create(byte[] arg);

    private static native void _destroy(long hdl);

    private static native long _parse(long hdl, byte[] str);

    private static native byte[] _version();
}

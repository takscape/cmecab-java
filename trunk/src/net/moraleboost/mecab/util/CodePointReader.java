/*
 **
 **  Feb. 1, 2009
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
package net.moraleboost.mecab.util;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * Readerをラップして、ひとつずつUnicodeコードポイントを
 * 取得するためのデコレータクラス。サロゲートペアを正しく認識する。<br>
 * 
 * 不正なサロゲートペアは、{@link #getAlternativeCodePoint()}で得られる
 * 代替コードポイントに置換される。
 */
public class CodePointReader
{
	/**
	 * 不正なサロゲートペアを置換する文字の既定値。
	 */
	public static final int DEFAULT_ALTERNATION_CODEPOINT = '〓';

	private PushbackReader reader = null;
	private long position = 0;
	private int alternationCodePoint = DEFAULT_ALTERNATION_CODEPOINT;
	private boolean eos = false;

	/**
	 * コードポイントイテレータを構築する。
	 * 
	 * @param sequence
	 *            ソースとなるcharのシーケンス
	 */
	public CodePointReader(Reader reader) {
		this.reader = new PushbackReader(reader, 1);
	}

	/**
	 * 不正なサロゲートペアを置換するための代替文字をセットする。 このメソッドを呼び出さない場合の既定値は、 「
	 * {@value #DEFAULT_ALTERNATIVE_CODEPOINT}」である。
	 * 
	 * @param cp
	 *            代替文字のコードポイント
	 */
	public void setAlternationCodePoint(int cp)
	{
		this.alternationCodePoint = cp;
	}

	/**
	 * 不正なサロゲートペアを置換するための代替文字を取得する。
	 * 
	 * @return 代替文字のコードポイント
	 */
	public int getAlternationCodePoint()
	{
		return alternationCodePoint;
	}
	
	/**
	 * キャラクタストリーム中の位置を返す。
	 * コードポイントでなくcharで数えるので、
	 * サロゲートペアが出現すると、位置は2大きくなる。
	 * 
	 * @return キャラクタストリーム中の位置。
	 */
	public long getPosition()
	{
		return position;
	}

	/**
	 * 次のコードポイントを取得する。
	 * 
	 * @return Unicodeコードポイント。
	 */
	public int read()
	throws IOException
	{
		int ci;
		char c, c2;
		
		if (eos) {
			return -1;
		}
		
		ci = reader.read(); ++position;
		
		if (ci < 0) {
			// end of character stream
			return -1;
		} else {
			c = (char)ci;
		}

		if (Character.isHighSurrogate(c)) {
			// 次の文字を検査
			ci = reader.read(); ++position;
			if (ci < 0) {
				// シーケンスがhigh surrogateで終わっている。
				// 代替文字を返すと共に、EOSフラグをONにする。
				eos = true;
				return alternationCodePoint;
			}
			
			c2 = (char)ci;
			if (Character.isLowSurrogate(c2)) {
				// サロゲートペアをコードポイントに変換して返す。
				return Character.toCodePoint(c, c2);
			} else {
				// high surrogateに続くcharが、low surrogateでない。
				// c2をプッシュバックして代替文字を返す。
				reader.unread(c2); --position;
				return alternationCodePoint;
			}
		} else if (Character.isLowSurrogate(c)) {
			// 単独で存在するlow surrogateを発見。
			// 代替文字を返す。
			return alternationCodePoint;
		} else {
			// 基本文字。そのまま返す。
			return c;
		}
	}
}

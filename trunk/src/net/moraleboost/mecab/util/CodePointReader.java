package net.moraleboost.mecab.util;

import java.io.IOException;

/**
 * 一つずつUnicodeコードポイントを
 * 取得するためのクラス。サロゲートペアを正しく認識する。<br>
 */
public interface CodePointReader
{
	/**
	 * 不正なサロゲートペアを置換するための代替文字をセットする。
	 * このメソッドを呼び出さない場合の既定値は、
	 * 「{@value #DEFAULT_ALTERNATIVE_CODEPOINT}」である。
	 * 
	 * @param cp
	 *            代替文字のコードポイント
	 */
	public abstract void setAlternationCodePoint(int cp);

	/**
	 * 不正なサロゲートペアを置換するための代替文字を取得する。
	 * 
	 * @return 代替文字のコードポイント
	 */
	public abstract int getAlternationCodePoint();

	/**
	 * キャラクタストリーム中の現在の位置を返す。
	 * コードポイント単位でなくchar単位で数えるので、
	 * サロゲートペアが出現すると、位置は2大きくなる。
	 * 
	 * @return キャラクタストリーム中の位置。
	 */
	public abstract long getPosition();

	/**
	 * 次のコードポイントを取得する。
	 * 
	 * @return Unicodeコードポイント。
	 */
	public abstract int read() throws IOException;
}

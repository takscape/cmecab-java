package net.moraleboost.mecab.util;

/**
 * CharSequenceを受け取り、ひとつずつUnicodeコードポイントを 取得するためのイテレータクラス。サロゲートペアを正しく認識する。<br>
 * 
 * 不正なサロゲートペアは、{@link #getAlternativeCodepoint()}で得られる 代替コードポイントに置換される。
 */
public class CodePointIterator
{
	/**
	 * 不正なサロゲートペアを置換する文字の既定値。
	 */
	public static final int DEFAULT_ALTERNATIVE_CODEPOINT = '〓';

	private CharSequence sequence = null;
	private int maxCharIndex = 0;
	private int charIndex = 0;
	private int alternativeCodepoint = DEFAULT_ALTERNATIVE_CODEPOINT;

	/**
	 * コードポイントイテレータを構築する。
	 * 
	 * @param sequence
	 *            ソースとなるcharのシーケンス
	 */
	public CodePointIterator(CharSequence sequence) {
		this.sequence = sequence;
		this.maxCharIndex = sequence.length();
	}

	/**
	 * 不正なサロゲートペアを置換するための代替文字をセットする。 このメソッドを呼び出さない場合の既定値は、 「
	 * {@value #DEFAULT_ALTERNATIVE_CODEPOINT}」である。
	 * 
	 * @param cp
	 *            代替文字のコードポイント
	 */
	public void setAlternativeCodepoint(int cp)
	{
		this.alternativeCodepoint = cp;
	}

	/**
	 * 不正なサロゲートペアを置換するための代替文字を取得する。
	 * 
	 * @return 代替文字のコードポイント
	 */
	public int getAlternativeCodepoint()
	{
		return alternativeCodepoint;
	}

	/**
	 * 次のコードポイントを取得する。
	 * 
	 * @return Unicodeコードポイント
	 */
	public int next()
	{
		char c = sequence.charAt(charIndex++);
		if (Character.isHighSurrogate(c)) {
			if (charIndex < maxCharIndex) {
				// 次の文字を検査
				char c2 = sequence.charAt(charIndex);
				if (Character.isLowSurrogate(c2)) {
					// サロゲートペアをコードポイントに変換して返す。
					++charIndex;
					return Character.toCodePoint(c, c2);
				} else {
					// high surrogateに続くcharが、low surrogateでない。
					// 代替文字を返す。
					return alternativeCodepoint;
				}
			} else {
				// シーケンスがhigh surrogateで終わっている。
				// 代替文字を返す。
				return alternativeCodepoint;
			}
		} else if (Character.isLowSurrogate(c)) {
			// 単独で存在するlow surrogateを発見。
			// 代替文字を返す。
			return alternativeCodepoint;
		} else {
			// 基本文字。そのまま返す。
			return c;
		}
	}

	/**
	 * 次のコードポイントが存在するかどうかを返す。
	 * 
	 * @return 次のコードポイントが存在すればtrue、そうでなければfalseを返す。
	 */
	public boolean hasNext()
	{
		return (charIndex < maxCharIndex);
	}
}

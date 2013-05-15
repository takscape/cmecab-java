/*
 **
 **  Mar. 22, 2008
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
package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class FeatureRegexFilter extends AbstractRegexFilter
{
    /**
     * トークンのタイプ属性
     */
    private TypeAttribute typeAttribute;
    /**
     * トークンの位置増加分属性
     */
    private PositionIncrementAttribute posIncAttribute;

    /**
     * typeが指定したパターンに合致するtokenをふるい落とすフィルタを構築する。
     * 
     * @param input
     *            上流TokenStream
     * @param stopPatterns
     *            Java正規表現の配列を指定。
     *            typeがこのパターンのいずれかにマッチするtokenはフィルタリングされる。
     */
    public FeatureRegexFilter(TokenStream input, String[] stopPatterns)
    {
        super(input, stopPatterns);
        typeAttribute = addAttribute(TypeAttribute.class);
        posIncAttribute = addAttribute(PositionIncrementAttribute.class);
    }

    @Override
    public final boolean incrementToken() throws IOException
    {
        int skippedPositions = 0;
        while (input.incrementToken()) {
            if (!match(typeAttribute.type())) {
                posIncAttribute.setPositionIncrement(
                        posIncAttribute.getPositionIncrement() + skippedPositions);
                return true;
            }
            skippedPositions += posIncAttribute.getPositionIncrement();
        }
        
        return false;
    }
    
    public void reset() throws IOException
    {
        super.reset();
        clearAttributes();
    }
}

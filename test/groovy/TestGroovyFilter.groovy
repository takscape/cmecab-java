package net.moraleboost.lucene.analysis

import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

class TestGroovyFilter extends TokenFilter
{
    private CharTermAttribute term
    private Map<String, String> args

    TestGroovyFilter(TokenStream ts, Map<String, String> args)
    {
        super(ts)
        term = addAttribute(CharTermAttribute.class)
        this.args = args
    }

    @Override
    boolean incrementToken()
    {
        while (input.incrementToken()) {
            term.setEmpty()
            term.append("replaced")
            return true
        }
        return false
    }
}

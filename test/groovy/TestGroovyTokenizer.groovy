package net.moraleboost.lucene.analysis

import foo.TestObject
import org.apache.lucene.analysis.Tokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute

class TestGroovyTokenizer extends Tokenizer
{
    private CharTermAttribute termAttr
    private TypeAttribute typeAttr
    private Iterator<String> iter

    TestGroovyTokenizer(Reader input, Map<String, String> args)
    {
        super(input)
        termAttr = addAttribute(CharTermAttribute.class)
        typeAttr = addAttribute(TypeAttribute.class)
        iter = Arrays.asList("本日", "は", "晴天", "なり", "。").iterator()
        TestObject obj = new TestObject()
        obj.test()
    }

    @Override
    boolean incrementToken()
    {
        while (iter.hasNext()) {
            termAttr.setEmpty()
            termAttr.append(iter.next())
            typeAttr.setType("term")
            return true
        }
        return false
    }
}

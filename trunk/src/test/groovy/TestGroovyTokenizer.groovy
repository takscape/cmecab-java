package net.moraleboost.lucene.analysis

import foo.TestObject
import org.apache.lucene.analysis.Tokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute
import org.apache.lucene.util.AttributeSource

class TestGroovyTokenizer extends Tokenizer
{
    private CharTermAttribute termAttr
    private TypeAttribute typeAttr
    private Iterator<String> iter

    TestGroovyTokenizer(AttributeSource.AttributeFactory factory, Reader input, Map<String, String> args)
    {
        super(factory, input)
        termAttr = addAttribute(CharTermAttribute.class)
        typeAttr = addAttribute(TypeAttribute.class)
        iter = Arrays.asList("本日", "は", "晴天", "なり", "。").iterator()
        TestObject obj = new TestObject()
        obj.test()
    }

    @Override
    final boolean incrementToken()
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

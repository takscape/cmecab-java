package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Util
{
    public static void addField(Document doc, String name, String value)
    {
        FieldType ft = new FieldType();
        ft.setTokenized(true);
        ft.setIndexed(true);
        ft.setStored(true);
        ft.setStoreTermVectors(true);

        Field field = new Field(name, value, ft);
        doc.add(field);
    }

    public static void compareTokens(TokenStream ts, String[] terms, int[][] offsets)
            throws IOException
    {
        int i = 0;

        CharTermAttribute termAttr =
                ts.getAttribute(CharTermAttribute.class);
        OffsetAttribute offAttr =
                ts.getAttribute(OffsetAttribute.class);

        while (ts.incrementToken()) {
            String term = new String(termAttr.buffer(), 0, termAttr.length());
            int startOff = offAttr.startOffset();
            int endOff = offAttr.endOffset();

            //System.out.println(term);
            assertEquals(terms[i], term);
            assertEquals("Wrong start offset", offsets[i][0], startOff);
            assertEquals("Wrong end offset", offsets[i][1], endOff);
            ++i;
        }

        assertEquals(terms.length, i);
    }

}

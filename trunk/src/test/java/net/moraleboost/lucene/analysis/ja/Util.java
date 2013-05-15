package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;

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
}

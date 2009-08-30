package net.moraleboost.lucene.analysis.ja;

import static org.junit.Assert.fail;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;


public class CJKAnalyzer2Test
{
    private RAMDirectory directory;
    private CJKAnalyzer2 analyzer;
    private IndexWriter writer;
    private IndexSearcher searcher;
    private QueryParser parser;
    
    @Test
    public void test() throws Exception
    {
        testHit(2, "本日は晴天なり。", "は晴天");
    }
    
    @Test
    public void test2() throws Exception
    {
        testHit(2, "かきつばた", "かき");
    }
    
    @Test
    public void test3() throws Exception
    {
        testHit(2, "かきつばたfoo", "ばたfoo");
    }
    
    private void testHit(int ngram, String text, String term) throws Exception
    {
        setUpWriter(ngram);
        addDocument("1", text);
        writer.commit();
        writer.optimize();
        writer.close();
        
        setUpSearcher();
        Query query = parser.parse(term);
        System.out.println("Parsed query = " + query.toString());
        TopDocs topDocs = searcher.search(query, 100);
        if (topDocs.scoreDocs.length <= 0) {
            fail("No hit.");
        }
    }
    
    private void setUpWriter(int ngram) throws Exception
    {
        directory = new RAMDirectory();
        analyzer = new CJKAnalyzer2(ngram);
        writer = new IndexWriter(directory, analyzer, new MaxFieldLength(4096));
    }
    
    private void setUpSearcher() throws Exception
    {
        searcher = new IndexSearcher(directory, true);
        parser = new QueryParser("text", analyzer);
        parser.setDefaultOperator(QueryParser.Operator.OR);
    }
    
    private void addDocument(String id, String text) throws Exception
    {
        Document doc = new Document();
        addField(doc, "id", id);
        addField(doc, "text", text);
        writer.addDocument(doc);
    }

    private void addField(Document doc, String name, String value)
    {
        Field field = new Field(name, value, Field.Store.COMPRESS,
                Field.Index.TOKENIZED, Field.TermVector.YES);
        doc.add(field);
    }
}

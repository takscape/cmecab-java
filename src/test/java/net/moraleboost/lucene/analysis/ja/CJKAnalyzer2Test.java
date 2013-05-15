package net.moraleboost.lucene.analysis.ja;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import static org.junit.Assert.fail;


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
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        writer = new IndexWriter(directory, config);
    }
    
    private void setUpSearcher() throws Exception
    {
        IndexReader reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
        parser = new QueryParser(Version.LUCENE_43, "text", analyzer);
        parser.setDefaultOperator(QueryParser.Operator.OR);
    }
    
    private void addDocument(String id, String text) throws Exception
    {
        Document doc = new Document();
        Util.addField(doc, "id", id);
        Util.addField(doc, "text", text);
        writer.addDocument(doc);
    }
}

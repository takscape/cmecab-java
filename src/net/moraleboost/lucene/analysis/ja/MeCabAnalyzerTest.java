package net.moraleboost.lucene.analysis.ja;

import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.RAMDirectory;

public class MeCabAnalyzerTest
{
    public static final String DIC_ENCODING = System
            .getProperty("net.moraleboost.mecab.encoding");
    
    private MeCabAnalyzer analyzer = null;
    private RAMDirectory directory = null;
    private IndexWriter writer = null;

    @Before
    public void setUp() throws Exception
    {
        //助詞をフィルタリングするAnalyzerを作成
        String[] filters = new String[] {
                "^助詞,.*$"
        };
        analyzer = new MeCabAnalyzer(DIC_ENCODING, "", filters);
        directory = new RAMDirectory();
        writer = new IndexWriter(directory, analyzer);

        // ドキュメント追加
        Document doc = new Document();
        addField(doc, "text", "本日は晴天なり。");
        writer.addDocument(doc);
        writer.flush();
        writer.optimize();
        writer.close();
    }
    
    @After
    public void tearDown() throws Exception
    {
        directory.close();
    }
    
    private void addField(Document doc, String name, String value)
    {
        Field field = new Field(
                name, value,
                Field.Store.COMPRESS, Field.Index.TOKENIZED, Field.TermVector.YES);
        doc.add(field);
    }
    
    @Test
    public void testAnalyze()
    {
        try {
            // タームベクタを取得
            IndexReader reader = IndexReader.open(directory);
            Term term = new Term("text", "晴天");
            TermDocs termDocs = reader.termDocs(term);
            if (!termDocs.next()) {
                fail("Index term not found.");
            }
            TermFreqVector vec = reader.getTermFreqVector(termDocs.doc(), "text");
            
            // vecに助詞が含まれていないことを確認。
            String[] indexTerms = vec.getTerms();
            for (String t: indexTerms) {
                if (t.equals("は")) {
                    fail("Filter not working.");
                }
                System.out.println("Index term = " + t);
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }
    
    @Test
    public void testSearch()
    {
        try {
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser("text", analyzer);
            parser.setDefaultOperator(QueryParser.Operator.OR);
            
            Query query = parser.parse("本日も晴天");
            System.out.println("Parsed query = " + query.toString());
            Hits hits = searcher.search(query);
            if (hits.length() <= 0) {
                fail("No hit.");
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}

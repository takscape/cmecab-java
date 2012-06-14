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

import static org.junit.Assert.fail;

import net.moraleboost.mecab.impl.StandardTagger;
import org.apache.lucene.index.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class StandardMeCabAnalyzerTest
{
    private StandardMeCabAnalyzer analyzer = null;
    private RAMDirectory directory = null;

    @Before
    public void setUp() throws Exception
    {
        // 助詞をフィルタリングするAnalyzerを作成
        String[] filters = new String[] { "^助詞,.*$" };
        analyzer = new StandardMeCabAnalyzer(new StandardTagger(""), Integer.MAX_VALUE, filters);
        directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_30, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        // ドキュメント追加
        Document doc = new Document();
        addField(doc, "text", "本日は晴天なり。");
        writer.addDocument(doc);
        writer.commit();
        writer.close();
    }

    @After
    public void tearDown() throws Exception
    {
        directory.close();
    }

    private void addField(Document doc, String name, String value)
    {
        Field field = new Field(name, value, Field.Store.YES,
                Field.Index.ANALYZED, Field.TermVector.YES);
        doc.add(field);
    }

    @Test
    public void testAnalyze()
    {
        try {
            // タームベクタを取得
            IndexReader reader = IndexReader.open(directory, true);
            Term term = new Term("text", "晴天");
            TermDocs termDocs = reader.termDocs(term);
            if (!termDocs.next()) {
                fail("Index term not found.");
            }
            TermFreqVector vec = reader.getTermFreqVector(termDocs.doc(),
                    "text");

            // vecに助詞が含まれていないことを確認。
            String[] indexTerms = vec.getTerms();
            for (String t : indexTerms) {
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
            IndexReader reader = IndexReader.open(directory, true);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser(Version.LUCENE_30, "text", analyzer);
            parser.setDefaultOperator(QueryParser.Operator.OR);

            Query query = parser.parse("本日も晴天");
            System.out.println("Parsed query = " + query.toString());
            TopDocs topDocs = searcher.search(query, 100);
            if (topDocs.scoreDocs.length <= 0) {
                fail("No hit.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}

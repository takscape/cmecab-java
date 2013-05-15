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

import net.moraleboost.mecab.impl.StandardTagger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

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
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        // ドキュメント追加
        Document doc = new Document();
        Util.addField(doc, "text", "本日は晴天なり。");
        writer.addDocument(doc);
        writer.commit();
        writer.close();
    }

    @After
    public void tearDown() throws Exception
    {
        directory.close();
    }

    @Test
    public void testAnalyze() throws Exception
    {
        // タームベクタを取得
        IndexReader reader = DirectoryReader.open(directory);
        for (AtomicReaderContext ar: reader.getContext().leaves()) {
            TermsEnum termsEnum = ar.reader().terms("text").iterator(null);
            BytesRef bytesRef;
            while ((bytesRef = termsEnum.next()) != null) {
                String t = bytesRef.utf8ToString();
                if (t.equals("は")) {
                    fail("Filter not working.");
                }
                System.out.println("Index term = " + t);
            }
        }
    }

    @Test
    public void testSearch() throws Exception
    {
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser(Version.LUCENE_43, "text", analyzer);
        parser.setDefaultOperator(QueryParser.Operator.OR);

        Query query = parser.parse("本日も晴天");
        System.out.println("Parsed query = " + query.toString());
        TopDocs topDocs = searcher.search(query, 100);
        if (topDocs.scoreDocs.length <= 0) {
            fail("No hit.");
        }
    }
}

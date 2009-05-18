/*
 **
 **  May. 17, 2009
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
package net.moraleboost.mecab.impl;

import static org.junit.Assert.*;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

import org.junit.Test;


public class LocalProtobufTaggerTest
{
    public static final String DIC_ENCODING = System
            .getProperty("net.moraleboost.mecab.encoding");

    @Test
    public void testParse()
    {
        try {
            Tagger tagger1 = new LocalProtobufTagger("");
            Tagger tagger2 = new StandardTagger(DIC_ENCODING, "");
            
            Node node1 = tagger1.parse("本日は晴天なり。");
            Node node2 = tagger2.parse("本日は晴天なり。");

            while (node1.hasNext()) {
                assertTrue(node2.hasNext());
                assertEquals(node1.next(), node2.next());
                assertEquals(node1.surface(), node2.surface());
                assertEquals(node1.feature(), node2.feature());
                assertEquals(node1.blank(), node2.blank());
                assertEquals(node1.posid(), node2.posid());
            }
            tagger1.close();
            tagger2.close();
        } catch (Exception e) {
            fail(e.toString());
        }
    }
    
    @Test
    public void testPerf()
    {
        try {
            Tagger tagger = new LocalProtobufTagger("");
            
            // warming up
            for (int i=0; i<100; ++i) {
                Node node = tagger.parse(StandardTaggerTest.TEXTS[i % StandardTaggerTest.TEXTS.length]);
    
                while (node.hasNext()) {
                    @SuppressWarnings("unused")
                    String surface = node.next();
                    @SuppressWarnings("unused")
                    String blank = node.blank();
                    @SuppressWarnings("unused")
                    String feature = node.feature();
                }
            }

            long start = System.currentTimeMillis();

            for (int i=0; i<1000; ++i) {
                Node node = tagger.parse(StandardTaggerTest.TEXTS[i % StandardTaggerTest.TEXTS.length]);
    
                while (node.hasNext()) {
                    @SuppressWarnings("unused")
                    String surface = node.next();
                    @SuppressWarnings("unused")
                    String blank = node.blank();
                    @SuppressWarnings("unused")
                    String feature = node.feature();
                }
            }
            
            long end = System.currentTimeMillis();
            
            System.out.println("Total: " + Long.toString(end-start) + " millis.");

            tagger.close();
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}

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

import static org.junit.Assert.fail;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

import org.junit.Test;


public class LocalProtobufTaggerTest
{
    @Test
    public void testParse()
    {
        try {
            Tagger tagger = new LocalProtobufTagger("");
            Node node = tagger.parse("本日は晴天なり。");

            while (node.hasNext()) {
                System.out.println("Surface = " + node.next());
                System.out.println("Feature = " + node.feature());
            }
            tagger.close();
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

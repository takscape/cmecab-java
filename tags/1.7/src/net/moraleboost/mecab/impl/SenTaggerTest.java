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
import net.java.sen.StringTagger;
import net.java.sen.Token;

import org.junit.Test;


public class SenTaggerTest
{
    @Test
    public void testPerf()
    {
        try {
            StringTagger tagger = StringTagger.getInstance("etc/sen/conf/sen.xml");

            // warming up
            for (int i=0; i<100; ++i) {
                Token[] tokens = tagger.analyze(StandardTaggerTest.TEXTS[i % StandardTaggerTest.TEXTS.length]);
    
                for (Token token: tokens) {
                    @SuppressWarnings("unused")
                    String surface = token.getSurface();
                    @SuppressWarnings("unused")
                    String feature = token.getPos();
                }
            }

            long start = System.currentTimeMillis();

            for (int i=0; i<1000; ++i) {
                Token[] tokens = tagger.analyze(StandardTaggerTest.TEXTS[i % StandardTaggerTest.TEXTS.length]);
    
                for (Token token: tokens) {
                    @SuppressWarnings("unused")
                    String surface = token.getSurface();
                    @SuppressWarnings("unused")
                    String feature = token.getPos();
                }
            }
            
            long end = System.currentTimeMillis();
            
            System.out.println("Total: " + Long.toString(end-start) + " millis.");
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}

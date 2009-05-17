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

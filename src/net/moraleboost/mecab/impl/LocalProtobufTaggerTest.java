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

            long start = System.currentTimeMillis();

            for (int i=0; i<1000; ++i) {
                Node node = tagger.parse(StandardTaggerTest.TEXTS[i % StandardTaggerTest.TEXTS.length]);
    
                while (node.hasNext()) {
                    String surface = node.next();
                    String rsurface = node.blank();
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

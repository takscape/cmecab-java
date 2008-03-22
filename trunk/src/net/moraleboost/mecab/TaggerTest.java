/*
 **
 **  Mar. 1, 2008
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
package net.moraleboost.mecab;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaggerTest
{
    public static final String DIC_ENCODING = System
            .getProperty("net.moraleboost.mecab.encoding");

    @Test
    public void testParse()
    {
        try {
            Tagger tagger = new Tagger(DIC_ENCODING, "");
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
    public void testParse2()
    {
        try {
            Tagger tagger = new Tagger(DIC_ENCODING, "");
            Node node = tagger.parse("本日は晴天なり。");
            tagger.close();

            // taggerが閉じられたあとは、nodeは無効化されなくてはならない。
            assertFalse(node.hasNext());
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testParse3()
    {
        try {
            Tagger tagger = new Tagger(DIC_ENCODING, "");
            Node node1 = tagger.parse("本日は晴天なり。");
            Node node2 = tagger.parse("働けども働けども我が暮し楽にならざりぢっと手を見る.");

            // 次にparseを呼び出した時点で、node1は無効化されなければならない。
            assertFalse(node1.hasNext());
        } catch (Exception e) {
            fail(e.toString());
        }
    }

}

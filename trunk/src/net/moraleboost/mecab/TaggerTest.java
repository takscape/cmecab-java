package net.moraleboost.mecab;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaggerTest {

	@Test
	public void testParse() {
		try {
			Tagger tagger = new Tagger("Shift_JIS", "");
			Node node = tagger.parse("本日は晴天なり。");
			
			while (node.hasNext()) {
				node.next();
			}
			tagger.close();
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testParse2() {
		try {
			Tagger tagger = new Tagger("Shift_JIS", "");
			Node node = tagger.parse("本日は晴天なり。");
			tagger.close();
			
			// taggerが閉じられたあとは、nodeは無効化されなくてはならない。
			assertFalse(node.hasNext());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testParse3() {
		try {
			Tagger tagger = new Tagger("Shift_JIS", "");
			Node node1 = tagger.parse("本日は晴天なり。");
			Node node2 = tagger.parse("働けども働けども我が暮し楽にならざりぢっと手を見る.");

			// 次にparseを呼び出した時点で、node1は無効化されなければならない。
			assertFalse(node1.hasNext());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

}

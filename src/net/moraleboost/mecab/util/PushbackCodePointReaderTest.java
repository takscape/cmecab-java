package net.moraleboost.mecab.util;

import static org.junit.Assert.*;

import java.io.CharArrayReader;
import java.io.IOException;

import org.junit.Test;

public class PushbackCodePointReaderTest
{
	@Test
	public void testBasic()
	throws Exception
	{
		String str = "abc";
        int scp = 0x00010400;

        CharArrayReader car = new CharArrayReader(str.toCharArray());
		CodePointReader base = new BasicCodePointReader(car);
		PushbackCodePointReader reader = new PushbackCodePointReader(base, 2);
		
		assertEquals((int)'a', reader.read()); assertEquals(1L, reader.getPosition());
		assertEquals((int)'b', reader.read()); assertEquals(2L, reader.getPosition());
		assertEquals((int)'c', reader.read()); assertEquals(3L, reader.getPosition());
		reader.unread((int)'あ', 1); assertEquals(2L, reader.getPosition());
		reader.unread(scp, 2); assertEquals(0L, reader.getPosition());
		assertEquals(scp, reader.read()); assertEquals(2L, reader.getPosition());
		assertEquals((int)'あ', reader.read()); assertEquals(3L, reader.getPosition());
		assertEquals(-1, reader.read());
	}
	
	@Test
	public void testMaxSize()
	throws Exception
	{
		String str = "abc";

        CharArrayReader car = new CharArrayReader(str.toCharArray());
		CodePointReader base = new BasicCodePointReader(car);
		PushbackCodePointReader reader = new PushbackCodePointReader(base, 2);
		
		for (int i=0; i<3; ++i) {
			reader.read();
		}
		for (int i=0; i<2; ++i) {
			reader.unread('a', 1);
		}
		
		try {
			reader.unread('a', 1);
			fail("スタックサイズ上限の指定が機能していません。");
		} catch (IOException e) {}
	}
}

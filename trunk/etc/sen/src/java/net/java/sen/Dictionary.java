/*
 * DoubleArrayTrie.java - Dictionary for lookup the morpheme.
 * 
 * Copyright (C) 2001, 2002 Taku Kudoh, Takashi Okamoto Taku Kudoh
 * <taku-ku@is.aist-nara.ac.jp> Takashi Okamoto <tora@debian.org>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * $Id: Dictionary.java,v 1.2 2004/06/07 11:33:12 tora Exp $
 */

package net.java.sen;

import java.io.IOException;

import net.java.sen.io.FileAccessor;
import net.java.sen.io.FileAccessorFactory;
import net.java.sen.util.DoubleArrayTrie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Dictionary {
  private static Log log = LogFactory.getLog(Dictionary.class);

  private FileAccessor tfd = null;
  private FileAccessor ffd = null;
  private DoubleArrayTrie da = new DoubleArrayTrie();
  private CToken result[] = new CToken[256];
  private int daresult[] = new int[256];
  private String charset = "utf-8";

  public boolean close() {
    return true;
  }

  public CToken[] commonPrefixSearch(char str[], int pos) throws IOException {
    int size = 0;
    if (log.isTraceEnabled()) {
      log.trace("input=" + new String(str, pos, str.length - pos));
      log.trace("pos=" + pos);
    }

    if (log.isTraceEnabled()) {
      log.trace("str.length=" + str.length);
      log.trace("pos2=" + pos);
    }

    int n = da.commonPrefixSearch(str, daresult, pos, 0);

    if (log.isTraceEnabled()) {
      log.trace("number of prefix = " + n);
    }

    for (int i = 0; i < n; i++) {
      int k = 0xff & daresult[i];
      int p = daresult[i] >> 8;
      //check p,k; >>
      if (log.isTraceEnabled()) {
        log.trace("token location = " + p);
      }

      //	tfd.seek((long)((p+3)*Token.SIZE));
      tfd.seek((int) ((p + 3) * CToken.SIZE));
      for (int j = 0; j < k; j++) {
        result[size] = new CToken();
        result[size].read(tfd);
        // following code may make bugs. please keep consistency
        // with MkChaDic
        if (log.isTraceEnabled()) {
          log.trace("----found----");
          log.trace("posInfo=" + this.getPosInfo(result[size].posID));
          log.trace("rcAttr2=" + result[size].rcAttr2);
          log.trace("rcAttr1=" + result[size].rcAttr1);
          log.trace("lcAttr=" + result[size].lcAttr);
          log.trace("length=" + result[size].length);
          log.trace("cost=" + result[size].cost);
        }

        size++;
      }

    }

    result[size] = null; // null terminate
    return result;
  }

  CToken[] exactMatchSearch(char str[], int pos) throws IOException {
    int size = 0;
    int len = str.length;
    if (log.isTraceEnabled()) {
      log.trace("len=" + len);
    }

    int n = da.search(str, pos, 0);

    if (n != -1) {
      int k = 0xff & n;
      int p = n >> 8;

      tfd.seek((int) ((p + 3) * CToken.SIZE));

      for (int i = 0; i < k; i++) {
        result[size] = new CToken();
        result[size].read(tfd);
        // following code may make bugs. please keep consistency
        // with MkChaDic
        result[size].length /= 2;
        if (log.isTraceEnabled()) {
          log.trace("rcAttr2=" + result[size].rcAttr2);
          log.trace("rcAttr1=" + result[size].rcAttr1);
          log.trace("lcAttr=" + result[size].lcAttr);
          log.trace("length=" + result[size].length);
          log.trace("str=" + new String(str, pos, result[size].length));
        }
        size++;
      }
    }
    result[size] = null; // null terminate
    return result;
  }

  public String getPosInfo(int f) {
    if (f == -1)
      return null;
    try {
      int cnt = 0;
      byte b[] = new byte[256];
      ffd.seek(f);
      // 2009-05-23
      // modified to avoid ArrayIndexOutOfBoundsException
      while (ffd.read(b, cnt, 1) != -1 && b[cnt] != (byte) '\0') {
        cnt++;
        if(b.length <= cnt) {
          byte new_b[]=new byte[b.length*2];
          for(int i=0;i<b.length;i++)
            new_b[i]=b[i];
            b=new_b;
        }
      }
      // modification end
      byte b2[] = new byte[cnt];
      for (int i = 0; i < cnt; i++)
        b2[i] = b[i];
      return new String(b2, charset);
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  public Dictionary(String tokenFile, String doubleArrayFile,
      String posInfoFile, String charset) throws IOException {
    long start;
    this.charset = charset;

    log.info("token file = " + tokenFile);
    start = System.currentTimeMillis();
    tfd = FileAccessorFactory.getInstance(tokenFile);
    log.info("time to load posInfo file = "
        + (System.currentTimeMillis() - start) + "[ms]");

    // load double array trie dictionary
    log.info("double array trie dictionary = " + doubleArrayFile);

    da.load(doubleArrayFile);

    // open pos infomation file.
    log.info("pos info file = " + posInfoFile);
    start = System.currentTimeMillis();
    ffd = FileAccessorFactory.getInstance(posInfoFile);
    log.info("time to load pos info file = "
        + (System.currentTimeMillis() - start) + "[ms]");
  }

  public CToken getBOSToken() throws IOException {
    tfd.seek(0);
    CToken t = new CToken();
    t.read(tfd);

    log.debug("getBOSToken()");
    log.debug("rcAttr2 = " + t.rcAttr2);
    log.debug("rcAttr1 = " + t.rcAttr1);
    log.debug("lcAttr = " + t.lcAttr);
    log.debug("posid = " + t.posid);
    log.debug("length = " + t.length);
    log.debug("cost = " + t.cost);
    log.debug("posID = " + t.posID);
    return t;
  }

  public CToken getEOSToken() throws IOException {
    tfd.seek((int) (CToken.SIZE * 1));
    CToken t = new CToken();
    t.read(tfd);

    log.debug("getEOSToken()");
    log.debug("rcAttr2 = " + t.rcAttr2);
    log.debug("rcAttr1 = " + t.rcAttr1);
    log.debug("lcAttr = " + t.lcAttr);
    log.debug("posid = " + t.posid);
    log.debug("length = " + t.length);
    log.debug("cost = " + t.cost);
    log.debug("posID = " + t.posID);
    return t;
  }

  public CToken getUnknownToken() throws IOException {
    tfd.seek((int) (CToken.SIZE * 2));
    CToken t = new CToken();
    t.read(tfd);

    log.debug("getEOSToken()");
    log.debug("rcAttr2 = " + t.rcAttr2);
    log.debug("rcAttr1 = " + t.rcAttr1);
    log.debug("lcAttr = " + t.lcAttr);
    log.debug("posid = " + t.posid);
    log.debug("length = " + t.length);
    log.debug("cost = " + t.cost);
    log.debug("posID = " + t.posID);

    return t;
  }
}

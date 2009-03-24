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

import java.io.IOException;

public class MeCabException extends IOException
{
    private static final long serialVersionUID = -4839770152212702083L;

    public MeCabException()
    {
        super();
    }

    public MeCabException(String msg)
    {
        super(msg);
    }

    public MeCabException(Throwable e)
    {
        super();
        initCause(e);
    }

    public MeCabException(String msg, Throwable e)
    {
        super(msg);
        initCause(e);
    }
}

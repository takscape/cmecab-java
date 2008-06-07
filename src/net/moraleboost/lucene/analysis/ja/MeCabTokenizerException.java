/*
**
**  Mar. 22, 2008
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
package net.moraleboost.lucene.analysis.ja;

public class MeCabTokenizerException extends RuntimeException
{
    private static final long serialVersionUID = 1790249223369360070L;

    public MeCabTokenizerException()
    {
        super();
    }

    public MeCabTokenizerException(String msg)
    {
        super(msg);
    }

    public MeCabTokenizerException(Throwable e)
    {
        super(e);
    }

    public MeCabTokenizerException(String msg, Throwable e)
    {
        super(msg, e);
    }
}

package net.moraleboost.lucene.analysis.ja;

public class MeCabTokenizerException extends RuntimeException
{
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

package net.moraleboost.mecab;

public class MeCabException extends Exception
{
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
        super(e);
    }
    
    public MeCabException(String msg, Throwable e)
    {
        super(msg, e);
    }
}

package net.moraleboost.mecab;

public interface DictionaryInfo
{
    int TYPE_SYS_DIC = 0;
    int TYPE_USR_DIC = 1;
    int TYPE_UNK_DIC = 2;

    String filename();
    String charset();
    long size();
    int type();
    long lsize();
    long rsize();
    int version();
    DictionaryInfo next();
}

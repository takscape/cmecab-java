package net.moraleboost.mecab;

public interface Node
{
    int TYPE_NOR_NODE = 0;
    int TYPE_UNK_NODE = 1;
    int TYPE_BOS_NODE = 2;
    int TYPE_EOS_NODE = 3;
    int TYPE_EON_NODE = 4;

    Node prev();
    Node next();
    Node enext();
    Node bnext();
    Path rpath();
    Path lpath();
    String surface();
    String rsurface();
    boolean leadingSpaceAndSurface(String[] leadingSpaceAndSurface);
    String feature();
    long id();
    int length();
    int rlength();
    int rcAttr();
    int lcAttr();
    int posid();
    int charType();
    int stat();
    boolean isbest();
    float alpha();
    float beta();
    float prob();
    short wcost();
    long cost();
}

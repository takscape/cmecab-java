package net.moraleboost.mecab;

public interface Path
{
    Node rnode();
    Path rnext();
    Node lnode();
    Path lnext();
    int cost();
    float prob();
}

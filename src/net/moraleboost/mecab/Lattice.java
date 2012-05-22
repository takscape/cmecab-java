package net.moraleboost.mecab;

public interface Lattice
{
    int REQ_TYPE_ONE_BEST = 1;
    int REQ_TYPE_NBEST = 2;
    int REQ_TYPE_PARTIAL = 4;
    int REQ_TYPE_MARGINAL_PROB = 8;
    int REQ_TYPE_ALTERNATIVE = 16;
    int REQ_TYPE_ALL_MORPHS = 32;
    int REQ_TYPE_ALLOCATE_SENTENCE = 64;

    void destroy();
    void clear();
    boolean isAvailable();
    Node bosNode();
    Node eosNode();
    Node beginNodes(long pos);
    Node endNodes(long pos);
    String sentence();
    void setSentence(String sentence);
    long size();
    double Z();
    void setZ(double Z);
    double theta();
    void setTheta(double theta);
    boolean next();
    int requestType();
    boolean hasRequestType(int requestType);
    void setRequestType(int requestType);
    void addRequestType(int requestType);
    void removeRequestType(int requestType);
    String toString();
    String enumNBestAsString(long N);
    String what();
}

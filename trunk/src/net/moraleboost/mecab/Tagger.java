package net.moraleboost.mecab;

public interface Tagger
{
    void destroy();
    Lattice createLattice();
    boolean parse(Lattice lattice);
    DictionaryInfo dictionaryInfo();
    String what();
    String version();
}

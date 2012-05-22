package net.moraleboost.mecab;

public interface Model
{
    void destroy();
    Tagger createTagger();
    Lattice createLattice();
    boolean swap(Model model);
    DictionaryInfo dictionaryInfo();
}

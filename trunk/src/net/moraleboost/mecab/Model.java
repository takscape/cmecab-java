package net.moraleboost.mecab;

public interface Model
{
    public void close();
    public Tagger createTagger() throws MeCabException;
    public Lattice createLattice() throws MeCabException;
}

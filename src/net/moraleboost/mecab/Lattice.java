package net.moraleboost.mecab;

import java.nio.charset.CharacterCodingException;

public interface Lattice
{
    public void close();
    public void setSentence(CharSequence text) throws CharacterCodingException;
    public Node bosNode() throws MeCabException, CharacterCodingException;
    public Node eosNode() throws MeCabException, CharacterCodingException;
}

package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class SenAnalyzer extends Analyzer
{
    private String confFile = null;
    private String[] stopPatterns = null;

    public SenAnalyzer(String confFile)
    {
        super();
        this.confFile = confFile;
    }
    
    public SenAnalyzer(String confFile, String[] stopPattens)
    {
        super();
        this.confFile = confFile;
        this.stopPatterns = stopPattens;
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
        try {
            TokenStream stream = new SenTokenizer(reader, this.confFile);

            if (stopPatterns != null) {
                stream = new FeatureRegexFilter(stream, stopPatterns);
            }

            return stream;
        } catch (IOException e) {
            throw new MeCabTokenizerException(e);
        }
    }
}

# 使用方法 #

  * 単体で使用する
  * Luceneから使用する
  * Solrから使用する

APIリファレンスは、ソースパッケージに同梱。

## 単体で使用する ##
  * StandardTaggerクラスをインスタンス化し、parseを呼び出す。
```
import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.StandardTagger;
import net.moraleboost.mecab.Node;

public static void main(String[] args) throws Exception
{
    // Taggerを構築。
    // 引数には、MeCabのcreateTagger()関数に与える引数を与える。
    StandardTagger tagger = new StandardTagger("");

    // バージョン文字列を取得
    System.out.println("MeCab version " + tagger.version());

    // Lattice（形態素解析に必要な実行時情報が格納されるオブジェクト）を構築
    Lattice lattice = tagger.createLattice();

    // 解析対象文字列をセット
    String text = "本日は晴天なり。";
    lattice.setSentence(text);

    // tagger.parse()を呼び出して、文字列を形態素解析する。
    tagger.parse(lattice);

    // 形態素解析結果を出力
    System.out.println(lattice.toString());

    // 一つずつ形態素をたどりながら、表層形と素性を出力
    Node node = lattice.bosNode();
    while (node != null) {
        String surface = node.surface();
        String feature = node.feature();
        System.out.println(surface + "\t" + feature);
        node = node.next();
    }

    // lattice, taggerを破壊
    lattice.destroy();
    tagger.destroy();
}
```

## スレッドセーフティについて ##
  * 複数のスレッドから、単一のTaggerインスタンスのparse()メソッドを安全に同時に呼び出せる。
  * Latticeは、スレッドごとに個別のインスタンスを持つ必要がある。

## Luceneから使用する ##
  * StandardMeCabAnalzyer、StandardMeCabTokenizer、FeatureRegexFilter等を直接インスタンス化して利用。
  * [テストケース](http://cmecab-java.googlecode.com/svn/trunk/src/net/moraleboost/lucene/analysis/ja/StandardMeCabAnalyzerTest.java)を参照。

## Solrから使用する ##
  * Solrのlibディレクトリ（プラグインディレクトリ）に、cmecab-(ver).jarと、bridj-0.6.1.jarをコピー。
  * schema.xmlに、StandardMeCabTokenizerFactory等を用いてフィールドタイプを定義。

```
<!-- MeCabを用いたTokenizer -->
<fieldType name="text_mecab" class="solr.TextField">
    <analyzer>
        <!-- charsetパラメータには、辞書の文字コードを明示的に指定できる。省略すると、辞書内に記録された文字コードを読み取って使用。 -->
        <tokenizer class="net.moraleboost.solr.StandardMeCabTokenizerFactory" charset="utf-8" />
    </analyzer>
</fieldType>

<!-- TinySegmenterを用いたTokenizer -->
<fieldType name="text_tinysegmenter" class="solr.TextField">
    <analyzer>
        <tokenizer class="net.moraleboost.solr.TinySegmenterTokenizerFactory" />
    </analyzer>
</fieldType>

<!-- CJKTokenizerクローン -->
<fieldType name="text_cjk" class="solr.TextField">
    <analyzer>
        <!-- ngramパラメータで、N-gramのNを指定。1ならunigram、2ならbigram、3ならtrigram。 -->
        <tokenizer class="net.moraleboost.solr.CJKTokenizer2Factory" ngram="2" />
    </analyzer>
</fieldType>
```
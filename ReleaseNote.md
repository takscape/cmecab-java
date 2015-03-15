## v2.0.1 (2012/6/14) ##

  * StandardMeCabTokenizerをSolrから使用すると、リクエストを繰り返すうちに使用メモリ量が単調に増加する問題に対処。
  * Groovyでフィルタ/トークナイザを書ける、GroovyFilterFactory / GroovyTokenizerFactoryを追加。
  * その他細々としたバグ修正。

## v2.0 (2012/5/23) ##

※※ 前のバージョンとの互換性が失われています ※※
  * Lucene/Solr 3.5+に対応。
  * MeCab 0.993以上を必要とするようになった。
  * BridJの導入により、ネイティブライブラリのビルドが不要になった。
  * Protocol Buffers, Senのコードを除去。Senについては、後継のlucene-gosenが存在し、パッケージに含める意義が薄れたため。
  * 半角カナの全角化、全角英数字の半角化を行うフィルタ、「JapaneseNormalizationFilter」を追加。

## v1.7 (2010/3/27) ##

  * Lucene2.9+/Solr 1.4に本対応。
  * ネイティブライブラリのビルドツールを、sconsに変更。
  * ネイティブライブラリを、Protocol Buffersに依存する部分と、しない部分に分割。Protocol Buffersがインストールされていなくても、ビルド可能になった。

## v1.6 (2009/5/25) ##

  * Solr 1.4向けに、CharStreamに対応した一連のTokenizer, TokenizerFactoryを追加。
  * パフォーマンス向上が見られないため、Pooled系のTokenizerを廃止。
  * senにバッファオーバーフロー防止パッチを当てたものを同梱。

## v1.5 (2009/5/19) ##

  * senを用いたTokenizer, Analyzerを追加。
  * StandardNode#posid()が一つ先のノードの値を返していたバグを修正。

## v1.4 (2009/5/17) ##

  * net.moraleboost.mecab.Taggerをインターフェースに変更。元のTaggerは、net.moraleboost.mecab.impl.StandardTaggerにリネーム。MeCabTokenizerFactory等の関連クラスも、すべてStandardMeCab～にリネーム。
  * Protocol Buffersを用いてネイティブライブラリとの通信を行うLocalProtobufTaggerを追加。StandardTaggerに比べ、約30%の高速化。
  * MeCabToken廃止。代わりに、Tokenのtypeに素性を格納するようにした。
  * 全般的に、Tokenizer/TokenFilterのnext()を、next(Token)に書き換え。

## v1.3 (2009/3/24) ##

  * TinySegmenterのJava移植版を追加。
  * TinySegmenterを用いたTokenizer, TokenizerFactoryを追加。

## v1.2 (2009/3/16) ##

  * CJKTokenizer2の区切り位置を、v1.0互換に戻した。
  * CJKTokenizer2が、半角カナを正しくトークナイズできない問題を修正。
  * CJKTokenizer2を、bigramだけでなく、N-gram一般に対応できるよう修正。
  * 試験的に、プールしたTaggerを使用するPooledMeCabTokenizerFactoryを追加。

## v1.1 (2009/2/17) ##

  * サロゲートペアを正しく認識するCJKTokenizer、CJKTokenizer2を追加。
  * MeCabTokenizerに空白文字や改行を含むテキストを与えると、TokenのstartOffset, endOffsetがずれる問題を修正。

## v1.0 ##

  * 初期リリース
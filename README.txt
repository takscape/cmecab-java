cmecab-java

1. これは何？

日本語形態素解析エンジンMeCab (http://mecab.sourceforge.net/) の
Javaバインディングです。SWIGを用いず、直接MeCabのライブラリを
呼び出しています。

おまけとして、以下のものを含んでいます。
 * MeCabを用いたLucene (http://lucene.apache.org/java/) 用のTokenizer、
   素性情報を用いてトークンをフィルタリングするTokenFilter、
   およびそれらを組み合わせたAnalyzer
 * 上をSolr (http://lucene.apache.org/solr/) から用いるための
   TokenizerFactoryおよびTokenFilterFactory
 * 任意のngramに対応したCJKTokenizerクローン
 * TinySegmenter (http://chasen.org/~taku/software/TinySegmenter/) の
   Java移植版、及びそのTokenizer、TokenizerFactory


2. パッケージ構成

配布パッケージは、以下のディレクトリ構造を持ちます。

bin  - ビルドされたJavaライブラリが書き出されるディレクトリ
lib  - ビルドおよびテストに必要なサードパーティライブラリが
      格納されたディレクトリ
src  - Pure Javaライブラリのソースコードが格納されたディレクトリ
test - テスト用データが格納されたディレクトリ
etc  - その他もろもろ（TinySegmenterのオリジナルソース等）


3. インストール方法

配布パッケージのルートディレクトリで、antを実行してください。

% ant

ビルドが終了すると、binディレクトリに、cmecab-(バージョン番号).jar
という名前のJARファイルが作成されます。これをお好きな場所にコピーして、
Javaのクラスパスを通してください。また、libディレクトリに含まれる、
bridj-(バージョン番号).jarにもクラスパスを通してください。

なお、事前に、MeCabのライブラリ（libmecab.dll, libmecab.soなど）に、
OSのパスが通しておく必要があります。


4. 利用方法
http://code.google.com/p/cmecab-java/wiki/HowToUse
をご覧ください。


5. ライセンス

cmecab-java本体はパブリックドメインとします。

ただし、TinySegmenter.java、TinySegmenterConstants.javaについては、
TinySegmenter (http://chasen.org/~taku/software/TinySegmenter/)の
二次的著作物であるため、オリジナルと同じく修正BSDライセンスに
従います。

なお、ビルドおよびテストのため、lib, etcディレクトリに以下のソフトウェアを
同梱しています。これらのソフトウェアは、それぞれのライセンスに従います。

* Apache Lucene/Solr
   * Apache License 2.0
   * lib/license/LICENSE-APACHE.txtをご覧ください
* JUnit
   * Common Public License 1.0
   * lib/license/cpl1.0.txtをご覧ください
* TinySegmenter
   * 修正BSDライセンス
   * lib/license/LICENSE-TinySegmenter.txtをご覧ください
* BridJ
   * 修正BSDライセンス
   * lib/license/LICENSE-BridJ.txtをご覧ください

6. 連絡先

MeCab、TinySegmenter、Lucene、Solr本体に関するご質問は、
それぞれのソフトウェアのメーリングリスト等へどうぞ。

cmecab-java自体に関するご質問等は、武田光平 k-tak@void.in までどうぞ。

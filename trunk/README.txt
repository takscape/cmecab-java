cmecab-java

1. これは何？

日本語形態素解析エンジンMeCab (http://mecab.sourceforge.net/) の
Javaバインディングです。SWIGを用いず、JNIから直接MeCabのライブラリを
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
 * sen (https://sen.dev.java.net/) を用いたTokenizer, Analyzer、
   およびそれらをSolrから用いるためのFactory


2. パッケージ構成

配布パッケージは、以下のディレクトリ構造を持ちます。

bin  - ビルドされたJavaライブラリが書き出されるディレクトリ
jni  - ネイティブライブラリのソースコードが格納されたディレクトリ
lib  - ビルドおよびテストに必要なサードパーティライブラリが
      格納されたディレクトリ
src  - Pure Javaライブラリのソースコードが格納されたディレクトリ
test - テスト用データが格納されたディレクトリ
etc  - その他もろもろ（senの設定ファイル、TinySegmenterのオリジナルソース等）


3. インストール方法

cmacab-javaは、以下の二つのパートからなります。
 * MeCabとJavaの橋渡しをするネイティブライブラリ
 * ネイティブライブラリを用いて動作するPure Javaライブラリ

以下、それぞれのインストール方法について説明します。

3.1. ネイティブライブラリのインストール

jniディレクトリに移動し、sconsを実行してください。
正しくビルドを完了するためには、protocol buffersおよびsconsが必要です。

なお、SConstructファイル内には、作者のビルド環境におけるJavaおよびMeCabの
パスが記載されています。必要に応じて書き換えてください。

ビルドが終了すると、カレントディレクトリに、CMeCab.dll(Windows)、
もしくはlibCMeCab.so(UNIX系)が作成されます。これをOSのパスの通った場所に
コピーしてください。

3.2. Javaライブラリのインストール

配布パッケージのルートディレクトリで、antを実行してください。

% ant

ビルドが終了すると、binディレクトリに、cmecab-(バージョン番号).jar
という名前のJARファイルが作成されます。これをお好きな場所にコピーして、
Javaのクラスパスを通してください。


4. 利用方法
http://code.google.com/p/cmecab-java/wiki/HowToUse
をご覧ください。


5. ライセンス

CMeCab本体はパブリックドメインとします。

ただし、TinySegmenter.java、TinySegmenterConstants.javaについては、
TinySegmenter (http://chasen.org/~taku/software/TinySegmenter/)の
二次的著作物であるため、オリジナルと同じく修正BSDライセンスに
従います。

なお、ビルドおよびテストのため、lib, etcディレクトリに以下のソフトウェアを
同梱しています。これらのソフトウェアは、それぞれのライセンスに従います。

* Apache Lucene 2.9-dev
   * Apache License 2.0
   * lib/license/LICENSE-APACHE.txtをご覧ください
* Apache Solr 1.4-dev
   * Apache License 2.0
   * lib/license/LICENSE-APACHE.txtをご覧ください
* Apache Commons Logging 1.1.1
   * Apache License 2.0
   * lib/license/LICENSE-APACHE.txtをご覧ください
* JUnit 4.4
   * Common Public License 1.0
   * lib/license/cpl1.0.txtをご覧ください
* TinySegmenter 0.1
   * 修正BSDライセンス
   * lib/license/LICENSE-TinySegmenter.txtをご覧ください
* Protocol Buffers 2.1.0
   * 修正BSDライセンス
   * lib/license/COPYING.txtをご覧ください
* sen 1.2.2.1に、バッファオーバーフロー対策パッチを当てたもの
   * GNU Lesser General Public License 2.1
   * lib/license/COPYING-sen.txtをご覧ください
   * 変更されたソースコードは、etc/sen/srcにあります

6. 連絡先

MeCab、TinySegmenter、Sen、Protocol Buffers、
Lucene、Solr本体に関するご質問は、
それぞれのソフトウェアのメーリングリスト等へどうぞ。

CMeCab自体に関するご質問等は、武田光平 k-tak@void.in までどうぞ。


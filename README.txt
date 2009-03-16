CMeCab

1. これは何？

日本語形態素解析エンジンMeCab (http://mecab.sourceforge.net/) の
Javaバインディングです。SWIGを用いず、JNIから直接MeCabのライブラリを
呼び出しています。

おまけとして、以下のものを含んでいます。
 * Lucene (http://lucene.apache.org/java/) 用のTokenizer、
   素性情報を用いてトークンをフィルタリングするTokenFilter、
   およびそれらを組み合わせたAnalyzer
 * Solr (http://lucene.apache.org/solr/) 用のTokenizerFactory
   およびTokenFilterFactory


2. パッケージ構成

配布パッケージは、以下のディレクトリ構造を持ちます。

bin  - ビルドされたJavaライブラリが書き出されるディレクトリ
jni  - ネイティブライブラリのソースコードが格納されたディレクトリ
lib  - ビルドおよびテストに必要なサードパーティライブラリが
      格納されたディレクトリ
src  - Pure Javaライブラリのソースコードが格納されたディレクトリ
test - テスト用データが格納されたディレクトリ


3. インストール方法

CMeCabは、以下の二つのパートからなります。
 * MeCabとJavaの橋渡しをするネイティブライブラリ
 * ネイティブライブラリを用いて動作するPure Javaライブラリ

以下、それぞれのインストール方法について説明します。

3.1. ネイティブライブラリのインストール

jniディレクトリに移動し、makeを実行してください。
Windows上のVisual Studioを用いるのであれば、以下のコマンドを実行します。

% nmake -f Makefile.win

Linux上のmakeおよびgccを利用する場合は、以下のコマンドを実行します。

% make -f Makefile.unix

なお、各Makefile内には、作者のビルド環境におけるJavaおよびMeCabの
パスが記載されています。必要に応じて、CMECAB_INCLUDE、CMECAB_LIBを
書き換えてください。

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

なお、ビルドおよびテストのため、libディレクトリに以下のソフトウェアを
同梱しています。これらのソフトウェアは、それぞれのライセンスに従います。

* Apache Lucene 2.4-dev
   * Apache License 2.0
* Apache Solr 1.3
   * Apache License 2.0
* JUnit 4.4
   * Common Public License 1.0


6. 連絡先

MeCab、Lucene、Solr本体に関するご質問は、
それぞれのソフトウェアのメーリングリスト等へどうぞ。

CMeCab自体に関するご質問等は、武田光平 k-tak@void.in までどうぞ。


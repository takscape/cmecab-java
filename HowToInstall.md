# インストール方法 #

## システム要件 ##
  * J2SE 1.5以上
  * Linux x86/amd64, Windows x86/x64, Solaris x86, MacOS X x86/x64/ppcのいずれか
  * Apache Ant
  * MeCab 0.993以上がインストールされていること（ただし、StandardTaggerを利用しない場合は不要）

## Javaライブラリの構築 ##
配布アーカイブのルートディレクトリでantを実行。

成功すれば、binディレクトリに、cmecab-(バージョン番号).jarができているはず。

## 実行テスト ##
StandardTaggerクラスのmain関数を呼び出して、形態素解析結果を表示してみる。Windowsにおいて、MeCabをC:\Program Files\MeCabにインストールし、またcmecab-java-2.0.1.zipの内容を、c:\cmecabに展開したと想定している。

```
set PATH=C:\Program Files\MeCab\bin;%PATH%
java -cp "c:\cmecab\cmecab-2.0.1.jar;c:\cmecab\lib\bridj-0.6.1.jar" net.moraleboost.mecab.impl.StandardTagger 本日は晴天なり。
```

StandardTaggerの引数には、解析対象文字列（上の例では「本日は晴天なり。」）を指定。成功すれば以下のような結果が表示される（IPA辞書を利用する場合）。
```
MeCab version 0.994

Original text: 本日は晴天なり。

Morphemes:
本日    名詞,副詞可能,*,*,*,*,本日,ホンジツ,ホンジツ
は      助詞,係助詞,*,*,*,*,は,ハ,ワ
晴天    名詞,一般,*,*,*,*,晴天,セイテン,セイテン
なり    助動詞,*,*,*,文語・ナリ,基本形,なり,ナリ,ナリ
。      記号,句点,*,*,*,*,。,。,。
EOS
```

動作しない場合は、以下を確認。
  * MeCabのライブラリ(libmecab.dll、libmecab.soなど）にパスが通っているか。
    * Javaのライブラリパスではなく、OSのパスを通す必要がある。つまり、WindowsではPath環境変数、Linuxではld.so.confやLD\_LIBRARY\_PATHで指定した場所。
# Tokenizer比較 #

CJKTokenizer2, StandardMeCabTokenizer, TinySegmenterTokenizerの比較。あくまで目安。

| **比較項目** | **CJKTokenizer2** | **StandardMeCabTokenizer** | **TinySegmenterTokenizer** |
|:-----------------|:------------------|:---------------------------|:---------------------------|
| 検索漏れの少なさ | ○ | △ | △ |
| 適合度（精度）の高さ | △ | ○ | ○ |
| 外部データ不要 | ○ | × | ○ |
| ユーザ辞書の追加が可能 | × | ○ | × |
| トークナイズ処理のパフォーマンス | ○ | △ | ○ |
| 日本語以外の言語の検索 | ○ | △ | × |


検索漏れを少なくしたい場合、CJKTokenizer2が向いている。ただし、ノイズは多くなる。また、CJKTokenizer2は記号を無視するので、記号を検索したい場合は不適。

一方、検索精度を重視する場合は、日本語の分かち書きに基づくトークナイズを行うStandardMeCabTokenizer、TinySegmenterTokenizerの方が適している。StandardMeCabTokenizerは、MeCabのライブラリを使用するため、別途MeCabがインストールされている必要がある。TinySegmenterTokenizerは、MeCabを必要としないが、ユーザ辞書の利用は不可能。

トークナイズのパフォーマンス面では、StandardMeCabTokenizerが不利。

CJKTokenizer2は、日本語以外の言語、たとえば英語、中国語、韓国語等にも適用可能。StandardMeCabTokenizerは、たとえばnaist-cdicなどの外国語用辞書をインストールすることにより、他の言語にも対応可能。TinySegmenterTokenizerは、他の言語で書かれたテキストには適用できない。
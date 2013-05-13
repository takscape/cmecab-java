/*
 **
 **  Mar. 1, 2008
 **
 **  The author disclaims copyright to this source code.
 **  In place of a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **
 **                                         Stolen from SQLite :-)
 **  Any feedback is welcome.
 **  Kohei TAKETA <k-tak@void.in>
 **
 */
package net.moraleboost.mecab.impl;

import static org.junit.Assert.*;

import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

import org.junit.Test;

public class StandardTaggerTest
{
    public static final String DIC_ENCODING = System
            .getProperty("net.moraleboost.mecab.encoding");
    
    public static String[] TEXTS = {
        "メロスは激怒した。必ず、かの邪智暴虐（じゃちぼうぎゃく）の王を除かなければならぬと決意した。メロスには政治がわからぬ。メロスは、村の牧人である。笛を吹き、羊と遊んで暮して来た。けれども邪悪に対しては、人一倍に敏感であった。きょう未明メロスは村を出発し、野を越え山越え、十里はなれた此（こ）のシラクスの市にやって来た。メロスには父も、母も無い。女房も無い。十六の、内気な妹と二人暮しだ。この妹は、村の或る律気な一牧人を、近々、花婿（はなむこ）として迎える事になっていた。結婚式も間近かなのである。メロスは、それゆえ、花嫁の衣裳やら祝宴の御馳走やらを買いに、はるばる市にやって来たのだ。先ず、その品々を買い集め、それから都の大路をぶらぶら歩いた。メロスには竹馬の友があった。セリヌンティウスである。今は此のシラクスの市で、石工をしている。その友を、これから訪ねてみるつもりなのだ。久しく逢わなかったのだから、訪ねて行くのが楽しみである。歩いているうちにメロスは、まちの様子を怪しく思った。ひっそりしている。もう既に日も落ちて、まちの暗いのは当りまえだが、けれども、なんだか、夜のせいばかりでは無く、市全体が、やけに寂しい。のんきなメロスも、だんだん不安になって来た。路で逢った若い衆をつかまえて、何かあったのか、二年まえに此の市に来たときは、夜でも皆が歌をうたって、まちは賑やかであった筈（はず）だが、と質問した。若い衆は、首を振って答えなかった。しばらく歩いて老爺（ろうや）に逢い、こんどはもっと、語勢を強くして質問した。老爺は答えなかった。メロスは両手で老爺のからだをゆすぶって質問を重ねた。老爺は、あたりをはばかる低声で、わずか答えた。",
        "メロスは、単純な男であった。買い物を、背負ったままで、のそのそ王城にはいって行った。たちまち彼は、巡邏（じゅんら）の警吏に捕縛された。調べられて、メロスの懐中からは短剣が出て来たので、騒ぎが大きくなってしまった。メロスは、王の前に引き出された。「この短刀で何をするつもりであったか。言え！」暴君ディオニスは静かに、けれども威厳を以（もっ）て問いつめた。その王の顔は蒼白（そうはく）で、眉間（みけん）の皺（しわ）は、刻み込まれたように深かった。「市を暴君の手から救うのだ。」とメロスは悪びれずに答えた。「おまえがか？」王は、憫笑（びんしょう）した。「仕方の無いやつじゃ。おまえには、わしの孤独がわからぬ。」「言うな！」とメロスは、いきり立って反駁（はんばく）した。「人の心を疑うのは、最も恥ずべき悪徳だ。王は、民の忠誠をさえ疑って居られる。」「疑うのが、正当の心構えなのだと、わしに教えてくれたのは、おまえたちだ。人の心は、あてにならない。人間は、もともと私慾のかたまりさ。信じては、ならぬ。」暴君は落着いて呟（つぶや）き、ほっと溜息（ためいき）をついた。「わしだって、平和を望んでいるのだが。」「なんの為の平和だ。自分の地位を守る為か。」こんどはメロスが嘲笑した。「罪の無い人を殺して、何が平和だ。」「だまれ、下賤（げせん）の者。」王は、さっと顔を挙げて報いた。「口では、どんな清らかな事でも言える。わしには、人の腹綿の奥底が見え透いてならぬ。おまえだって、いまに、磔（はりつけ）になってから、泣いて詫（わ）びたって聞かぬぞ。」「ああ、王は悧巧（りこう）だ。自惚（うぬぼ）れているがよい。私は、ちゃんと死ぬる覚悟で居るのに。命乞いなど決してしない。ただ、――」と言いかけて、メロスは足もとに視線を落し瞬時ためらい、「ただ、私に情をかけたいつもりなら、処刑までに三日間の日限を与えて下さい。たった一人の妹に、亭主を持たせてやりたいのです。三日のうちに、私は村で結婚式を挙げさせ、必ず、ここへ帰って来ます。」「ばかな。」と暴君は、嗄（しわが）れた声で低く笑った。「とんでもない嘘（うそ）を言うわい。逃がした小鳥が帰って来るというのか。」「そうです。帰って来るのです。」メロスは必死で言い張った。「私は約束を守ります。私を、三日間だけ許して下さい。妹が、私の帰りを待っているのだ。そんなに私を信じられないならば、よろしい、この市にセリヌンティウスという石工がいます。私の無二の友人だ。あれを、人質としてここに置いて行こう。私が逃げてしまって、三日目の日暮まで、ここに帰って来なかったら、あの友人を絞め殺して下さい。たのむ、そうして下さい。」",
        "それを聞いて王は、残虐な気持で、そっと北叟笑（ほくそえ）んだ。生意気なことを言うわい。どうせ帰って来ないにきまっている。この嘘つきに騙（だま）された振りして、放してやるのも面白い。そうして身代りの男を、三日目に殺してやるのも気味がいい。人は、これだから信じられぬと、わしは悲しい顔して、その身代りの男を磔刑に処してやるのだ。世の中の、正直者とかいう奴輩（やつばら）にうんと見せつけてやりたいものさ。「願いを、聞いた。その身代りを呼ぶがよい。三日目には日没までに帰って来い。おくれたら、その身代りを、きっと殺すぞ。ちょっとおくれて来るがいい。おまえの罪は、永遠にゆるしてやろうぞ。」「なに、何をおっしゃる。」「はは。いのちが大事だったら、おくれて来い。おまえの心は、わかっているぞ。」"
    };

    @Test
    public void testParse()
    {
        try {
            Tagger tagger = new StandardTagger("");
            Lattice lattice = tagger.createLattice();
            lattice.setSentence("本日は晴天なり。");
            tagger.parse(lattice);
            Node node = lattice.bosNode().next();

            while (node != null && node.stat() != Node.TYPE_EOS_NODE) {
                System.out.println("Surface = " + node.surface());
                System.out.println("Feature = " + node.feature());
                node = node.next();
            }
            lattice.destroy();
            tagger.destroy();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testPerf()
    {
        try {
            Tagger tagger = new StandardTagger("");
            Lattice lattice = tagger.createLattice();
            String[] leadingSpaceAndSurface = new String[2];

            // warming up
            for (int i=0; i<100; ++i) {
                lattice.clear();
                lattice.setSentence(TEXTS[i % TEXTS.length]);
                tagger.parse(lattice);
                Node node = lattice.bosNode().next();

                while (node != null && node.stat() != Node.TYPE_EOS_NODE) {
                    node.leadingSpaceAndSurface(leadingSpaceAndSurface);
                    String feature = node.feature();
                    node = node.next();
                }
            }

            long start = System.currentTimeMillis();

            for (int i=0; i<1000; ++i) {
                lattice.clear();
                lattice.setSentence(TEXTS[i % TEXTS.length]);
                tagger.parse(lattice);
                Node node = lattice.bosNode().next();

                while (node != null && node.stat() != Node.TYPE_EOS_NODE) {
                    node.leadingSpaceAndSurface(leadingSpaceAndSurface);
                    String feature = node.feature();
                    node = node.next();
                }
            }
            
            long end = System.currentTimeMillis();
            
            System.out.println("Total: " + Long.toString(end-start) + " millis.");

            lattice.destroy();
            tagger.destroy();
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}

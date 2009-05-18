#!/usr/bin/perl

# IPA dic to MeCab Converter for ipadic-2.5.0 ipadic-2.5.1
# $Id: ipa2mecab.pl.in,v 1.5 2003/04/14 15:15:36 taku-ku Exp $;

my $PREFIX = shift (@ARGV) || ".";
$PREFIX =~ s#/$##g;

sub escape
{
    my $s = $_[0];
    if ($s =~ /[",]/) {
	$s =~ s/"/""/g;
        return "\"$s\"";
    }
    return $s;
}

sub strip
{
    my $a = $_[0];
    $a =~ s/^ //g; 
    $a =~ s/ $//g; 
    $a =~ s/^\(//g; 
    $a =~ s/\)$//g; 
    return $a;
}

sub append 
{
    my ($a, $b) = @_;
    return $b if ($a eq ""); 
    return $a if ($b eq "*" || $b eq "");
    return $a . $b;
}

sub get1st 
{
    my $s = $_[0];
    if (defined $s && $s =~ /^{([^}]+)}([^\}]*)$/) {
	return (split /\//, $1)[0] . $2;
     } else {
        return $s;
     }
}

sub conv 
{
    my $str   = $_[0];
    my @tmp   = split /,/, $str;
    my $ctype = $tmp[6];

    if (! defined $CTYPE{$ctype}) { 
	return "$str\n";
	next;
    }

    my $lex   = $tmp[0];
    my $base  = $tmp[8];
    my $read  = $tmp[9];
    my $pron  = $tmp[10];

    my $lexs  = $lex;
    my $reads = $read;
    my $prons = $pron;

    my @list = @{$CTYPE{$ctype}};
    if ($list[0][1] ne "*") {
	$lexs  = substr ($lex,  0, length ($lex)  - length($list[0][1]));
	$reads = substr ($read, 0, length ($read) - length($list[0][2]));
	$prons = substr ($pron, 0, length ($pron) - length($list[0][2]));
    }

    my $result = "";
    for my $i (0..$#list) {
	my $cform   = $list[$i][0];
	my $newlex  = &append ($lexs,  $list[$i][1]);
	my $newread = &append ($reads, $list[$i][2]);
	my $newpron = &append ($prons, $list[$i][2]);
	next if (length ($newlex) <= 1);
	$result .= "$newlex,$tmp[1],$tmp[2],$tmp[3],$tmp[4],$tmp[5],$tmp[6],$cform,$base,$newread,$newpron\n";
    }

    return $result;
}

open (F, "$PREFIX/connect.cha") || die "Fatal: $PREFIX/connect.cha cannot open\n";
open (S, "> connect.csv") || die "FATAL: connect.csv cannot open\n";
while (<F>) {
    next if (/^;/ || /^$/);
    chomp;

    s/ (\d+)\)//;
    my $score = $1;
    my @tmp = split /\) \(/, $_;

    my @out;
    for (@tmp) {
	my @pos;
	if (/\(+([^\(\)]+)\)/) {
	    @pos = split / /, $1;
	}

	my @other;
	if (/\)+ ([^\)]+)\)+$/) {
	    my $t = $1;
	    $t =~ s/^ //g;
	    $t =~ s/ $//g;
	    @other = split / /, $t;
	}

	for my $i ($#pos + 1 .. 3) {
	    $pos[$i] = "*";
	}

	for my $i ($#other + 1 .. 2) {
	    $other[$i] = "*";
	}

	push @pos, @other;
	push @out, join ",", @pos;
    }

    @out = ("*,*,*,*,*,*,*", @out) if (@out == 2);

    print S "\"$out[0]\",\"$out[1]\",\"$out[2]\",$score\n";
}
close (F);
close (S);

my $ctype = "";
my @CTYPE;
open (F, "$PREFIX/cforms.cha") || die "Fatal: $PREFIX/cforms.cha cannot open\n";
while (<F>) {
    chomp;
    next if (/^;/ || /^$/ || /語幹/);
    if (/^\((\S+)\s*$/) {
	$ctype = $1;
    } elsif (/^\)\s*$/) {
	$ctype = "";
    } elsif ($ctype ne "" && /^\s+\(([^\)]+)\)/) {
	my ($a,$b,$c) = split /\s+/, $1;
	push @{$CTYPE{$ctype}}, [(&strip($a), &strip($b), &strip($c))];
    }
}
close (F);

opendir (DICDIR, $PREFIX) || die "FATAL: $PREFIX cannot open\n";
my @dic = grep (/\.dic$/, readdir (DICDIR));
open (S, "> dic.csv") || die "FATAL: dic.txt cannot open\n";

for my $file (@dic) {
    print STDERR "$PREFIX/$file ...\n";
    open (F, "$PREFIX/$file") || die "FATAL: $PREFIX/$file cannot open\n";
    while (<F>) {
	next if (/^;/ || /^$/);
	chomp;
	my $lex; 

	if (/\((LEX|見出し語) \(([^ ]+) (\d+)/) {
	    $lex = $2;
	    $score = $3;
	}
	next if ($lex eq "");

	my $read;
	my $pron;
	my $pos;
	my $ctype;

	$read  = $2 if (/\((READING|読み) ([^\)0-9]+)/);
	$pron  = $2 if (/\((PRON|発音) ([^\)0-9]+)/);
	$pos   = $2 if (/\((POS|品詞) \(([^-\)0-9]+)/);
	$ctype = $2 if (/\((CTYPE|活用型) ([^\)0-9]+)/);
	
	$read = &get1st ($read);
	$pron = &get1st ($pron);
	
	my @posl = split / /, $pos;
	my $pos1 = $posl[0] || "*";
	my $pos2 = $posl[1] || "*";
	my $pos3 = $posl[2] || "*";
	my $pos4 = $posl[3] || "*";
	$ctype ||= "*";
	$pron  ||= $read;
	$base  = $lex;

        $lex = escape($lex);
        $pos1 = escape($pos1);
        $pos2 = escape($pos2);
        $pos3 = escape($pos3);
        $pos4 = escape($pos4);
 	$ctype = escape($ctype);
 	$base = escape($base);
 	$read = escape($read);
 	$pron = escape($pron);
	
	print S &conv ("$lex,$score,$pos1,$pos2,$pos3,$pos4,$ctype,*,$base,$read,$pron");
    }
    close (F);
}
close (S);

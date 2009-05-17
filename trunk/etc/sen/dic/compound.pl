#!/usr/bin/perl

# IPA dic to MeCab Converter for ipadic-2.5.0 ipadic-2.5.1
# $Id: compound.pl,v 1.2 2004/10/14 02:45:16 tfukui Exp $;

my $PREFIX = shift (@ARGV) || ".";
$PREFIX =~ s#/$##g;

sub append 
{
    my ($a, $b) = @_;
    return $b if ($a eq ""); 
    return $a if ($b eq "*" || $b eq "");
    return $a . $b;
}

sub conv 
{
    my $str   = $_[0];
    my $ref_compounds = $_[1];
    my @compounds = @$ref_compounds;
    my @tmp   = split /,/, $str;
    my $ctype = $tmp[6];

    my $str2;
    my @tmp2;
    my $ctype2;

    if (@compounds > 0) {
	$str2 = @compounds[@compounds-1];
	@tmp2 = split /,/, $str2;
	$ctype2 = $tmp2[6];
    }

    # 活用を展開する必要が無いときは, そのまま出力    
    if (! defined $CTYPE{$ctype}) {
	my $com_str = "*";
	if (@compounds > 0) {
	    $com_str = "\"@compounds\"";
#	    print "$com_str\n";
	} 
	return "$str,$com_str\n";
	next;
    }

    my $lex   = $tmp[0];
    my $base  = $tmp[8];
    my $read  = $tmp[9];
    my $pron  = $tmp[10];

    my $lexs  = $lex;
    my $reads = $read;
    my $prons = $pron;

    # 語幹だけ残す
    my @list = @{$CTYPE{$ctype}};
    if ($list[0][1] ne "*") {
	$lexs  = substr ($lex,  0, length ($lex)  - length($list[0][1]));
	$reads = substr ($read, 0, length ($read) - length($list[0][2]));
	$prons = substr ($pron, 0, length ($pron) - length($list[0][2]));
    }

    my $lex2;
    my $base2;
    my $read2;
    my $pron2;
    my $lexs2;
    my $reads2;
    my $prons2;
    
    if (@compounds > 0) {
	$lex2   = $tmp2[0];
	$base2  = $tmp2[8];
	$read2  = $tmp2[9];
	$pron2  = $tmp2[10]; 
	
	$lexs2  = $lex2;
	$reads2 = $read2;
	$prons2 = $pron2;
	
	my @list2 = @{$CTYPE{$ctype2}};
	if ($list2[0][1] ne "*") {
	    $lexs2  = substr ($lex2,  0, length ($lex2)  - length($list2[0][1]));
	    $reads2 = substr ($read2, 0, length ($read2) - length($list2[0][2]));
	    $prons2 = substr ($pron2, 0, length ($pron2) - length($list2[0][2]));
	}
    }

    # 展開!
    my $result = "";
    for my $i (0..$#list) {
	my $cform   = $list[$i][0];
	my $newlex  = &append ($lexs,  $list[$i][1]);
	my $newread = &append ($reads, $list[$i][2]);
	my $newpron = &append ($prons, $list[$i][2]);
	next if (length ($newlex) <= 1);

	my $com_str = "*";
	if (@compounds > 0) {
	    my $cform2   = $list[$i][0];
	    my $newlex2  = &append ($lexs2,  $list2[$i][1]);
	    my $newread2 = &append ($reads2, $list2[$i][2]);
	    my $newpron2 = &append ($prons2, $list2[$i][2]);
#	    @compounds[@compounds-1] = "$newlex2,*,$tmp[2],$tmp[3],$tmp[4],$tmp[5],$tmp[6],$cform2,$base2,$newread2,$newpron2";
#	    $com_str = "\"@compounds\"";
	    @tmp = ($newlex2,"*",$tmp[2],$tmp[3],$tmp[4],$tmp[5],$tmp[6],$cform2,$base2,$newread2,$newpron2);
	    @compounds[@compounds-1] = join ',', map {(s/"/""/g or /[\r\n,]/) ? qq("$_") : $_} @tmp;
	    $com_str = join ',', map {(s/"/""/g or /[\r\n,]/) ? qq("$_") : $_} @compounds;
	}
	
#	$result .= "$newlex,$tmp[1],$tmp[2],$tmp[3],$tmp[4],$tmp[5],$tmp[6],$cform,$base,$newread,$newpron,$com_str\n";
	@result1 = ( $newlex,$tmp[1],$tmp[2],$tmp[3],$tmp[4],$tmp[5],$tmp[6],$cform,$base,$newread,$newpron,$com_str );
	$result = join ',', map {(s/"/""/g or /[\r\n,]/) ? qq("$_") : $_} @result1;
    }

    return $result."\n";
}

opendir (DICDIR, $PREFIX) || die "FATAL: $PREFIX cannot open\n";
my @dic = grep (/\.dic$/, readdir (DICDIR));
open (S, "> compound.csv") || die "FATAL: dic.txt cannot open\n";

for my $file (@dic) {
    print STDERR "$PREFIX/$file ...\n";
    open (F, "$PREFIX/$file") || die "FATAL: $PREFIX/$file cannot open\n";

    $next = "";
    while (1) {
	if ($next eq "") {
	    $_ = <F>;
	    last if (!$_);
	    next if (/^;/ || /^$/);
	    chomp;
	} else {
	    $_ = $next;
	    if (/^;/ || /^$/) {
		$next = "";
		next;
	    }
	}
#	print "$_\n";
	my $lex; 
	
	if (/\((LEX|見出し語) \(([^ ]+)\s+(\d+)/) {
	    $lex = $2;
	    $score = $3;
	}
#	print "lex = $lex\n";
#	print "score = $score\n";
	if (!defined $lex) {
	    $next = "";
	    next;
	}
	
	my $read;
	my $pron;
	my $pos;
	my $ctype;
	my @compounds = ();

	$read  = $2 if (/\((READING|読み)\s+([^\)]+)/);
	$pron  = $2 if (/\((PRON|発音)\s+([^\)]+)/);
	$pos   = $2 if (/\((POS|品詞)\s+\(([^-\)]+)/);
	$ctype = $2 if (/\((CTYPE|活用型)\s+([^-\)]+)/);

	my @posl = split / /, $pos;
	my $pos1 = $posl[0] || "*";
	my $pos2 = $posl[1] || "*";
	my $pos3 = $posl[2] || "*";
	my $pos4 = $posl[3] || "*";
	$ctype ||= "*";
	$pron  ||= $read;
	$base  = $lex;
#	$str = "$lex,$score,$pos1,$pos2,$pos3,$pos4,$ctype,*,$base,$read,$pron";
	@tmp = ($lex,$score,$pos1,$pos2,$pos3,$pos4,$ctype,"*",$base,$read,$pron);
	$str = join ',', map {(s/"/""/g or /[\r\n,]/) ? qq("$_") : $_} @tmp;
	
	$next = "";
	$count = 0;
	while (<F>) {
	    if (/^ /) {
		chomp;
		next unless (/\((POS|品詞) /);
		my $lex; 
		
		if (/\((LEX|見出し語)\s+([^)]+)/) {
		   $lex = $2;
	        } 
		next if (! $lex);    
		my $read;
		my $pron;
		my $pos;
		my $ctype;
		my $extra;
	        $read  = $2 if (/\((READING|読み)\s+([^\)]+)/);
	        $pron  = $2 if (/\((PRON|発音)\s+([^\)]+)/);
                $pos   = $2 if (/\((POS|品詞)\s+\(([^-\)]+)/);
                $ctype = $2 if (/\((CTYPE|活用型)\s+([^-\)]+)/);
	        $extra = "-";
                $extra = $2 if (/\((付加情報)\s+([^\)]+)/);
		my @posl = split / /, $pos;
		my $pos1 = $posl[0] || "*";
		my $pos2 = $posl[1] || "*";
		my $pos3 = $posl[2] || "*";
		my $pos4 = $posl[3] || "*";
		$ctype ||= "*";
		$pron  ||= $read;
		$base  = $lex;

#		@compounds[$count++] = "$lex,*,$pos1,$pos2,$pos3,$pos4,$ctype,"*",$base,$read,$pron,$extra";
		@tmp = ($lex,"*",$pos1,$pos2,$pos3,$pos4,$ctype,"*",$base,$read,$pron,$extra);
		@compounds[$count++] = join ',', map {(s/"/""/g or /[\r\n,]/) ? qq("$_") : $_} @tmp;

		
#		print &conv ("$lex,*,$pos1,$pos2,$pos3,$pos4,$ctype,*,$base,$read,$pron");
	    } else {
		$next = $_;
		last;
	    }
	}
        if (@compounds > 0) {
          print S &conv ($str, \@compounds);
        }
    }
    close (F);
}
close (S);

/*
 **
 **  Mar. 24, 2009
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
var segmenter = new TinySegmenter();
var BIAS = segmenter.BIAS__;

function getKeys(name) {
	var ret = "";
	var obj = segmenter[name + "__"];

	for (var k in obj) {
		if (ret != "") {
			ret += ",";
		}
		ret += ("\"" + k + "\"");
	}
	
	return "{" + ret + "}";
}

function getValues(name) {
	var ret = "";
	var obj = segmenter[name + "__"];

	for (var k in obj) {
		if (ret != "") {
			ret += ",";
		}
		ret += obj[k];
	}
	
	return "{" + ret + "}";
}

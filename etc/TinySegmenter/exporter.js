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

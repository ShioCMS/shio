if (typeof importClass != "function") {
	load("nashorn:mozilla_compat.js");
}

var shObject = Java.type('com.viglet.shiohara.js.shObject');

var viglet = this.viglet || {};
viglet.shiohara = viglet.shiohara || {};

viglet.shiohara.shObject = function() {

	this.getTest = function() {
		return "Hello World";
	}, this.getTestClass = function() {
		return shObject.test();
	}

};

viglet.shiohara.shobject = new viglet.shiohara.shObject();
if (typeof importClass != "function") {
	load("nashorn:mozilla_compat.js");
}

var shObject = Java.type('com.viglet.shiohara.js.shObject');
var shNavigationComponent = spring.getBean('shNavigationComponent', Java
		.type('com.viglet.shiohara.component.ShNavigationComponent'));
var shQueryComponent = spring.getBean('shQueryComponent', Java
		.type('com.viglet.shiohara.component.ShQueryComponent'));

var viglet = this.viglet || {};
viglet.shiohara = viglet.shiohara || {};

viglet.shiohara.shObject = function() {

	this.getTest = function() {
		return "Hello World";
	}, this.getTestClass = function() {
		return shObject.test();
	}, this.navigation = function(siteName, home) {
		return Java.from(shNavigationComponent.navigation(siteName, home));
	}, this.query = function(channelId, postTypeName) {
		return Java.from(shQueryComponent.findByChannelName(channelId, postTypeName));
	}

};

viglet.shiohara.shobject = new viglet.shiohara.shObject();
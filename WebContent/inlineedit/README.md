# jQuery Inline/In-Place Edit Plugin

This started out as a really simple tutorial piece on how to build your own In-Placed editor with jQuery. 
See original post at http://yelotofu.com/2009/08/jquery-inline-edit-tutorial/

But as interest in something simple grew so has the code. This plugin has the following supported features:

* Uses the event delegation variant of .on() so you do not need to re-bind new DOM elements in-order to make them editable
* legacy support for .live()
* supports input and textarea elements. other elements supported by extending $.inlineEdit.prototype.controls
* custom placeholder text for empty areas, defaults to "Click to edit"
* custom callbacks and cancellable actions via return false.
* keyboard support. ENTER to save and ESC to cancel.
* Can be loaded as an AMD module.
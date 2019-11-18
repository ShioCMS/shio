tinymce.PluginManager.add('shTinyMCE', function (editor, url) {

    editor.addButton('shMailTo', {
        title: 'Insert e-mail link',
        //cmd: 'mceMailTo',
        image: '/img/widget/tinymce/email-icon.png',
        onclick: function() {
            var linkText = editor.selection.getContent({ format: 'text' });
            // Open window
            editor.windowManager.open({
              title: 'Insert e-mail link',              
              body: [
                {type: 'textbox', size: 40, name: 'email', label: 'e-mail'},
                {type: 'textbox', size: 40, name: 'textDisplay', label: 'Text to display', value : editor.selection.getContent({ format: 'text' })}
              ],
              onsubmit: function(e) {
               
                var newText = "<a href='mailto:" + e.data.email + "'>" + e.data.textDisplay + "</a>"
                editor.execCommand('mceInsertContent', false, newText);
              }
            });
          }

    });

    // Add a button that opens a window
    editor.addButton('shAddImage', {
        tooltip: 'Insert Shiohara image',
        icon: true,
        image: '/img/widget/tinymce/image-icon.png',
        onclick: function () {

            var scope = angular.element(document.getElementById('idForJS')).scope();
            scope.modalExplorer().then(function (rest) {
                if (rest != null) {
                    editor.insertContent('<img src="' + rest.url + '" alt = "' + rest.label + '"/>');

                }
            });
        }
    });
    editor.addButton('shAddContent', {
        tooltip: 'Insert Shiohara content',
        icon: true,
        image: '/img/widget/tinymce/content-icon.png',
        onclick: function () {
            var linkText = editor.selection.getContent({ format: 'text' });
            var scope = angular.element(document.getElementById('idForJS')).scope();
            scope.selectContent().then(function (rest) {
                if (rest != null) {
                    var newText = '<a href="' + rest.url + '"/>' + rest.label + '</a>';
                    if (linkText !== null && linkText.trim().length > 0) {
                        newText = '<a href="' + rest.url + '"/>' + linkText + '</a>';
                    }
                    editor.execCommand('mceInsertContent', false, newText);
                }
            });
        }
    });
    // Adds a menu item to the tools menu
    editor.addMenuItem('shAddImage', {
        text: 'Example plugin',
        context: 'tools',
        onclick: function () {
            // Open window with a specific url
            editor.windowManager.open({
                title: 'TinyMCE site',
                url: 'http://www.tinymce.com',
                width: 400,
                height: 300,
                buttons: [{
                    text: 'Close',
                    onclick: 'close'
                }]
            });
        }
    });
});
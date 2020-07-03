tinymce.PluginManager.add('shTinyMCE', function (editor, url) {

    editor.ui.registry.addButton('shMailTo', {
        tooltip: 'Insert e-mail link',
        //cmd: 'mceMailTo',
        icon: 'link',
        onAction: function () {
            // Open window
            editor.windowManager.open({
                title: 'Insert e-mail link',
                body: {
                    type: 'panel',
                    items: [{ type: 'input', size: 40, name: 'email', label: 'e-mail' },
                    { type: 'input', size: 40, name: 'textDisplay', label: 'Text to display', value: editor.selection.getContent({ format: 'text' }) }] // array of panel components
                },
                buttons: [ // A list of footer buttons
                    {
                        type: 'cancel',
                        name: 'closeButton',
                        text: 'Cancel'
                    },
                    {
                        type: 'submit',
                        name: 'submitButton',
                        text: 'Insert Email',
                        primary: true
                    }
                ],
                onSubmit: function (api) {
                    var data = api.getData();
                    var newText = "<a href='mailto:" + data.email + "'>" + data.textDisplay + "</a>"
                    tinymce.activeEditor.execCommand('mceInsertContent', false, newText);
                    api.close();
                }
            });
        }

    });

    // Add a button that opens a window
    editor.ui.registry.addButton('shAddImage', {
        tooltip: 'Insert Shio image',
        icon: 'image',
        onAction: function () {

            var scope = angular.element(document.getElementById('idForJS')).scope();
            scope.modalExplorer().then(function (rest) {
                if (rest != null) {
                    editor.insertContent('<img src="' + rest.url + '" alt = "' + rest.label + '"/>');

                }
            });
        }
    });
    editor.ui.registry.addButton('shAddContent', {
        tooltip: 'Insert Shio content',
        icon: 'embed-page',
        onAction: function () {
            var linkText = editor.selection.getContent({ format: 'text' });
            var scope = angular.element(document.getElementById('idForJS')).scope();
            scope.selectContent().then(function (rest) {
                if (rest != null) {
                    var newText = '<a href="' + rest.url + '"/>' + rest.label + '</a>';
                    if (linkText !== null && linkText.trim().length > 0) {
                        newText = '<a href="' + rest.url + '"/>' + linkText + '</a>';
                    }
                    tinymce.activeEditor.execCommand('mceInsertContent', false, newText);
                }
            });
        }
    });
});
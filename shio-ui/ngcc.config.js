module.exports = {
    packages: {
      'ngx-ace-wrapper': {
        ignorableDeepImportMatchers: [
          /brace\//,
        ]
      },
      '@ckeditor/ckeditor5-angular': {
        ignorableDeepImportMatchers: [
          /@ckeditor\//,
        ]
      }
    }
  };
$('input.tags').tagsinput({
  typeahead: {
    source: function(query) {
      return $.getJSON('http://japode.prj.viglet.co/services/cities.json');
    }
  }
});
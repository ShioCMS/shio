<form class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="field0"></label>
    <div class="controls">
     <label id="field0" name="field0">CTD1</label>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="field1">Name</label>
    <div class="controls">
      <input type="text" id="field1" name="field1" value="CTD1">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="field2">Description</label>
    <div class="controls">
      <textarea id="field2" name="field2">Conteúdo Simples</textarea>
    </div>
  </div>
  <div class="form-actions">
    <div class="btn-group"> <a href="newcontent.php" class="btn btn-primary">Save changes</a> <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
      <ul class="dropdown-menu">
        <li><a href="#"><i class="icon-trash"></i> Delete</a></li>
        <li><a href="duplicatecontent.php"><i class="icon-plus-sign"></i> Duplicate</a></li>
      </ul>
    </div>
    <button type="button" class="btn">Cancel</button>
  </div>
  </div>
</form>

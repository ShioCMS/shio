      <ul class="nav nav-list">
        <li class="nav-header">Model</li>
        <li 
<?php if ((htmlspecialchars($_GET["section"]) == 'modelBasic') || (is_null($_GET["section"]))) { ?>
 class="active"
<?php } ?>
>

<a href="editcontent.php?section=modelBasic">Basic Information</a></li>
        <li

<?php if (htmlspecialchars($_GET["section"]) == 'modelAttribs') { ?>
 class="active"
<?php } ?>
><a href="editcontent.php?section=modelAttribs">Atributes</a></li>

        <li class="nav-header">Look & Feel</li>
        <li><a href="#">Style</a></li>
        <li><a href="#">Templates</a></li>
        
      </ul>

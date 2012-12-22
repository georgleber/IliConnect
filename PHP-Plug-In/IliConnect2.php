<?php

## Die POST Variablen muessen gesetzt sein, damit die ilAuthFactory Klasse die Authentifizierung gegen ILIAS macht.
## Zum debuggen im Browser, habe ich diese beiden Zeilen hinzugefuegt.
$_POST['username'] = $_REQUEST['user'];
$_POST['password'] = $_REQUEST['pass'];

  include_once 'Services/Authentication/classes/class.ilAuthFactory.php';
  ilAuthFactory::setContext(ilAuthFactory::CONTEXT_SOAP);

  require_once("Services/Init/classes/class.ilInitialisation.php");
  $ilInit = new ilInitialisation();
  $ilInit->initILIAS('webdav');

  $login=$ilUser->getLogin();
  include_once "Services/IliConnect/classes/class.IliConnect.php";
  require_once('classes/class.ilObjectFactory.php');
  require_once('Services/Utilities/classes/class.ilUtil.php');
  require_once('Modules/Exercise/classes/class.ilObjExerciseAccess.php');
  require_once('Modules/Exercise/classes/class.ilObjExerciseGUI.php');
  require_once('Modules/Exercise/classes/class.ilExAssignment.php');
  $ilIConnect =  new IliConnect();

?>

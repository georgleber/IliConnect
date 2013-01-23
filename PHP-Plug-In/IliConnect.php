<?php

## Die POST Variablen muessen gesetzt sein, damit die ilAuthFactory Klasse die Authentifizierung gegen ILIAS macht.
## Zum debuggen im Browser, habe ich diese beiden Zeilen hinzugefuegt.
if(isset($_REQUEST['user']) && isset($_REQUEST['pass'])) {
  $_POST['username'] = $_REQUEST['user'];
  $_POST['password'] = $_REQUEST['pass'];
}

if(isset($_POST['action'])) {
  $_REQUEST['action'] = $_POST['action'];
}

  include_once 'Services/Authentication/classes/class.ilAuthFactory.php';
  ilAuthFactory::setContext(ilAuthFactory::CONTEXT_SOAP);

  require_once("Services/Init/classes/class.ilInitialisation.php");
  $ilInit = new ilInitialisation();
  $ilInit->initILIAS('webdav');

  $login=$ilUser->getLogin();
  if($login) {
    include_once "Services/IliConnect/classes/class.IliConnect.php";
    require_once('classes/class.ilObjectFactory.php');
    require_once('Services/Utilities/classes/class.ilUtil.php');
    require_once('Modules/Exercise/classes/class.ilObjExerciseAccess.php');
    require_once('Modules/Exercise/classes/class.ilObjExerciseGUI.php');
    require_once('Modules/Exercise/classes/class.ilExAssignment.php');
    require_once('Services/Tracking/classes/class.ilChangeEvent.php');
    $ilIConnect =  new IliConnect();
  } else {
    sleep(1);
    die("ACCESS_DENIED");
  }
?>

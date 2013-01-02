<?php

if(!defined('IliConnect')) die(); // Zugriff nur ueber IliConnect.php

## Die POST Variablen muessen gesetzt sein, damit die ilAuthFactory Klasse die Authentifizierung gegen ILIAS macht.
## Zum debuggen im Browser, habe ich diese beiden Zeilen hinzugefuegt.
$_POST['username'] = $_REQUEST['user'];
$_POST['password'] = $_REQUEST['pass'];

include_once 'Services/Authentication/classes/class.ilAuthFactory.php';
ilAuthFactory::setContext(ilAuthFactory::CONTEXT_SOAP);

require_once("Services/Init/classes/class.ilInitialisation.php");
$ilInit = new ilInitialisation();
$ilInit->initILIAS('webdav');

require_once('classes/class.ilObjectFactory.php');
require_once('Modules/Exercise/classes/class.ilObjExerciseAccess.php');
require_once('Modules/Exercise/classes/class.ilObjExerciseGUI.php');
require_once('Modules/Exercise/classes/class.ilExAssignment.php');


function printCurrentDesk($ilUser) {
  ## Header auf XML stellen
  header ("Content-Type:text/xml");  
  
  ## MAIN XML TAG (auch XML Root genannt)
  $xml = new SimpleXMLElement("<Iliconnect/>");

  ## XML ILICONNECT CURRENT
  $current = $xml->addChild("Current");

  ## XML ILICONNECT CURRENT NOTIFICATIONS
  $notifications = $current->addChild("Notifications");

  ## XML ILICONNECT CURRENT DESKTOP
  $desktop = $current->addChild("Desktop");

  ## PERSOENLICHER SCHREIBTISCH AUSGEBEN
  $desktop_items = $ilUser->getDesktopItems();
  
  ## Tree ist eine ILIAS eigene Variable
  #global $tree;

  ## In $desktop_items sind nun ILIAS Objekte.
  foreach($desktop_items as $item) {
    desktopItem2Xml($item,$desktop,$notifications);
  }
  
  ## AUSGABE DER XML STRUKTUR
  echo $xml->asXML();
}

function printMagazin($ilUser) {
  ## Header auf XML stellen
  header ("Content-Type:text/xml");

  ## MAIN XML TAG (auch XML Root genannt)
  $xml = new SimpleXMLElement("<Iliconnect/>");

  ## XML ILICONNECT CURRENT
  $current = $xml->addChild("Current");

  ## XML ILICONNECT CURRENT NOTIFICATIONS
  $notifications = $current->addChild("Notifications");

  ## XML ILICONNECT CURRENT DESKTOP
  $desktop = $current->addChild("Desktop");

  ## PERSOENLICHER SCHREIBTISCH AUSGEBEN
  $desktop_items = $ilUser->getDesktopItems();
  
  ## Tree ist eine ILIAS eigene Variable
  global $tree;
  $desktop_items= $tree->getChilds(1);

  ## In $desktop_items sind nun ILIAS Objekte.
  foreach($desktop_items as $item) {
    desktopItem2Xml($item,$desktop, new SimpleXMLElement("<dontshow/>"));
  }

  ## AUSGABE DER XML STRUKTUR
  echo $xml->asXML();
}

## Der (un)uebersichtlichkeit halber in eine eigene funktion gepackt.
## erzeugt einen eintrag innerhalb des NOTIFICATIONS tag
function notification2Xml($a,$nxml)
{
  $notify = $nxml->addChild("Notification");
  $notify->addChild("title",$a["title"]);
  #$notify->addChild("date",$assarray->getDeadline());
  $notify->addChild("ref_id",$a["ref_id"]);
  $notify->addChild("description",$a["description"]);
}


## FUNCTION FUER DIE REKURSIVE ABARBEITUNG DER CHILD ITEMS
function desktopItem2Xml($array,$sxml,$notifications){
  if(strstr("file|fold|crs|tst|exc",$array["type"]))
  {
    $item = $sxml->addChild("Item");
    $item->addChild("title",$array[title]);
    $item->addChild("description",$array[description]);
    $item->addChild("type",$array[type]);
    $item->addChild("ref_id",$array[ref_id]);

    if($array[type] == "exc")
    {
      notification2Xml($array,$notifications);
    }
    
    global $tree;
    $children=$tree->getChilds($array[ref_id]);
    foreach($children as $child)
    {
      desktopItem2Xml($child,$item,$notifications);
    }
  } else {
    # Debugging
    #print_r($array);
  }
}


switch($_GET["action"]) {
  case "sync":
    printCurrentDesk($ilUser);
    break;
  case "magazin":
    printMagazin($ilUser);
    break;
  case "search":
    #suche
    break;
  default:
    echo "ACTION_UNKNOWN";
    break;
}

?>

<?php

## Die POST Variablen muessen gesetzt sein, damit die ilAuthFactory Klasse die Authentifizierung gegen ILIAS macht.
## Zum debuggen im Browser, habe ich diese beiden Zeilen hinzugefuegt.
$_POST['username'] = 'root'; #$_REQUEST['user'];
$_POST['password'] = 'homer'; #$_REQUEST['pass'];

include_once 'Services/Authentication/classes/class.ilAuthFactory.php';
ilAuthFactory::setContext(ilAuthFactory::CONTEXT_SOAP);

require_once("Services/Init/classes/class.ilInitialisation.php");
$ilInit = new ilInitialisation();
$ilInit->initILIAS('webdav');


# Stuff
require_once('classes/class.ilObjectFactory.php');
require_once('Modules/Exercise/classes/class.ilObjExerciseAccess.php');
require_once('Modules/Exercise/classes/class.ilObjExerciseGUI.php');
require_once('Modules/Exercise/classes/class.ilExAssignment.php');


## Header auf XML stellen
header ("Content-Type:text/xml");  
$login=$ilUser->getLogin();


if(strstr($_SERVER['REQUEST_URI'],"register") > 0) {

## register xy..

};


## MAIN XML TAG (auch XML Root genannt)
$xml = new SimpleXMLElement("<Iliconnect/>");

## XML ILICONNECT CURRENT
$current = $xml->addChild("Current");

## XML ILICONNECT CURRENT NOTIFICATIONS
$notifications = $current->addChild("Notifications");

## XML ILICONNECT CURRENT DESKTOP
$desktop = $current->addChild("Desktop");

## PERSOENLICHER SCHREIBTISCH AUSGEBEN
$desktop_items= $ilUser->getDesktopItems();
# Tree ist eine ILIAS eigene Variable
global $tree;

## KOMPLETTES MAGAZIN AUSGEBEN
#$desktop_items= $tree->getChilds(1);

## In $desktop_items sind nun ILIAS Objekte.
foreach($desktop_items as $item) {
  desktopItem2Xml($item,$desktop,$notifications);
}


#global $ilDB;
#$q = "SELECT obj_id,ref_id FROM object_reference WHERE ".$ilDB->in("ref_id", array(63), false, 'integer');
#$r = $ilDB->query($q);
#$elem=$ilDB->fetchAssoc($r);

#print_r($r);
#print_r($elem);

#$factory=new ilObjectFactory();
#$object=$factory->getInstanceByRefId(63);


#$assarray = new ilExAssignment($object);
#print_r($ass);

#$test = new ilExAssignment(63);

#print_r($object->getOwner());

#print_r($object);
#$notify = $notifications->addChild("Notification");
#$notify->addChild("title",$assarray->getTitle());
#$notify->addChild("description",$assarray->getDescription());
#$notify->addChild("date",$assarray->getDeadline());
#$notify->addChild("ref_id",$assarray->getId());


## AUSGABE DER XML STRUKTUR
echo $xml->asXML();

#global $lng, $ilUser;

#$props = array();
#$rem = ilObjExerciseAccess::_lookupRemainingWorkingTimeString($object->obj_id);
#$props[] = array(
#"property" => $object->lng->txt("exc_next_deadline"),
#"value" => ilObjExerciseAccess::_lookupRemainingWorkingTimeString($object->obj_id)
#);

#print_r($rem);

#print_r($object);
#$filesize=$this->format_bytes($object->getFileSize());

#$ex_gui =& new ilObjExerciseGUI("", (int) $_GET["ref_id"], true, false);

#print_r($ex_gui->read());


## Der (un)uebersichtlichkeit halber in eine eigene funktion gepackt.
## erzeugt einen eintrag innerhalb des NOTIFICATIONS tag
function notification2Xml($a,$nxml)
{
  #print_r($a);
  
  $factory=new ilObjectFactory();
  $obj=$factory->getInstanceByRefId(61);#$a['ref_id']);
  
  print_r($obj);
  die();
   
  $ass = new ilExAssignment($a["ref_id"]);
#  $ass->setExerciseId($a["ref_id"]);
#  $ass->read();
  
  #print_r($ass->getDeadline());
die();
  $assarray = $n->getDeadline();
  #print_r($n);
  
  #print_r($n);
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

## OLD STUFF
function array_to_xml($student_info, &$xml_student_info) {
    foreach($student_info as $key => $value) {
        if(is_array($value)) {
            if(!is_numeric($key)){
                $subnode = $xml_student_info->addChild("$key");
                array_to_xml($value, $subnode);
            }
            else{
                array_to_xml($value, $xml_student_info);
            }
        }
        else {
	  if(!is_numeric($key)){
            $xml_student_info->addChild($key,"$value");
	  } else {
	    $xml_student_info->addChild("key_".$key,"$value");
	  }
        }
    }
}
?>

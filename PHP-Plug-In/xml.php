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

## Header auf XML stellen
header ("Content-Type:text/xml");  
$login=$ilUser->getLogin();

## MAIN XML TAG (auch XML Root genannt)
$xml = new SimpleXMLElement("<Iliconnect/>");

## XML ILICONNECT CURRENT
$current = $xml->addChild("Current");

## XML ILICONNECT CURRENT NOTIFICATIONS
$notifications = $current->addChild("Notifications");

## XML ILICONNECT CURRENT DESKTOP
$desktop = $current->addChild("Desktop");


################################################################
## TEST STUFF (eMails, etc)
/*
$mails = $xml->addChild("mails");
$news  = $xml->addChild("news");

include_once ("Services/Mail/classes/class.ilMailbox.php");
include_once ("Services/Mail/classes/class.ilMail.php");
$mailbox = new ilMailbox($ilUser->getId());
$mailObj = new ilMail($ilUser->getId());

$inboxId = $mailbox->getInboxFolder();
$inbox = $mailObj->getMailsOfFolder($inboxId);
foreach($inbox as $mailitem){
  $mail = $mailObj->getMail($mailitem['mail_id']);
  $sender = new ilObjUser($mailitem['sender_id']);
  $sender = $sender->getFullname();
  //Name added
  $mail['sender_name'] = $sender;
  
  $mailelem = $mails->addChild('mail');
  array_to_xml($mail,$mailelem);
}
// Mails are ok		


echo $xml->asXML();

#echo $xml;

//die();
include_once("Services/News/classes/class.ilNewsItem.php");
$newsarr      = ilNewsItem::_lookupUserPDPeriod($ilUser->getId());
$newsitems = ilNewsItem::_getNewsItemsOfUser($ilUser->getId(), false, false, $newsarr);

foreach($newsitems as $newsitem) {
  $newselem = $news->addChild('newsitem');
  array_to_xml($newsitem,$newselem);
}
*/

##
################################################################



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

## GIBT DIE XML AUS
#echo $xml->asXML();


#print_r($children);


#global $ilDB;
#$q = "SELECT obj_id,ref_id FROM object_reference WHERE ".$ilDB->in("ref_id", array(63), false, 'integer');
#$r = $ilDB->query($q);
#$elem=$ilDB->fetchAssoc($r);

#print_r($r);
#print_r($elem);

require_once('classes/class.ilObjectFactory.php');
require_once('Modules/Exercise/classes/class.ilObjExerciseAccess.php');
require_once('Modules/Exercise/classes/class.ilObjExerciseGUI.php');
require_once('Modules/Exercise/classes/class.ilExAssignment.php');

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
  $factory=new ilObjectFactory();
  $exc=$factory->getInstanceByRefId($a[ref_id]);
  require_once('Modules/Exercise/classes/class.ilExAssignment.php');
  $n = new ilExAssignment($exc);
#print_r($n);
  $notify = $nxml->addChild("Notification");
  $notify->addChild("title",$n->getTitle());
  #$notify->addChild("title",$assarray->getTitle());
  #$notify->addChild("date",$assarray->getDeadline());
  #$notify->addChild("ref_id",$array[ref_id]);
  #$notify->addChild("description",$array["description"]);#assarray->getId());
  #$assarray = 0;
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

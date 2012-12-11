<?php

$_POST['username'] = $_REQUEST['user'];
$_POST['password'] = $_REQUEST['pass'];

#if(!empty($_GET['client_id'])) $client_id = $_GET['client_id'];
#if(!empty($_GET['client_id'])) $_COOKIE["ilClientId"] = $_GET['client_id'];

include_once 'Services/Authentication/classes/class.ilAuthFactory.php';
ilAuthFactory::setContext(ilAuthFactory::CONTEXT_SOAP);

require_once("Services/Init/classes/class.ilInitialisation.php");
$ilInit = new ilInitialisation();
$ilInit->initILIAS('webdav');

header ("Content-Type:text/xml");  
$login=$ilUser->getLogin();

# MAIN XML TAG
$xml = new SimpleXMLElement("<Iliconnect/>");
# CURRENT
$current = $xml->addChild("Current");
$desktop = $current->addChild("Desktop");

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



#desktopItem2Xml($ilUser->getDesktopItems(),$desktop);

$desktop_items= $ilUser->getDesktopItems();
global $tree;

$desktop_items= $tree->getChilds(1);

foreach($desktop_items as $item) {
  desktopItem2Xml($item,$desktop);
}


echo $xml->asXML();


#print_r($children);


#global $ilDB;
#$q = "SELECT obj_id,ref_id FROM object_reference WHERE ".$ilDB->in("ref_id", array(49), false, 'integer');
#$r = $ilDB->query($q);
#$elem=$ilDB->fetchAssoc($r);

#print_r($r);
#print_r($elem);

require_once('classes/class.ilObjectFactory.php');
$factory=new ilObjectFactory();
$object=$factory->getInstanceByRefId(63);

#print_r($object);
#$filesize=$this->format_bytes($object->getFileSize());

$ex_gui =& new ilObjExerciseGUI("", (int) $_GET["ref_id"], true, false);

print_r($ex_gui);

function desktopItem2Xml($array,$sxml){
  if(strstr("file|fold|crs|tst|exc",$array["type"]))
  {
    $item = $sxml->addChild("Item");
    $item->addChild("title",$array[title]);
    $item->addChild("description",$array[description]);
    $item->addChild("type",$array[type]);
    $item->addChild("ref_id",$array[ref_id]);
    global $tree;
    $children=$tree->getChilds($array[ref_id]);
    foreach($children as $child)
    {
      desktopItem2Xml($child,$item);
    }
  } else {
    # Debugging
    #print_r($array);
  }
}

// function defination to convert array to xml
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

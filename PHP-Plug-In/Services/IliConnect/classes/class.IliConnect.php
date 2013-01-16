<?php

class IliConnect{
  
  function __construct(){
    $ref_id = intval($_GET['ref_id']);

    switch($_GET["action"]) {
      case "join":
        $course = $this->checkCourse($ref_id);
        echo $this->joinCourse($course, isset($_REQUEST["course_pw"]) ? $_REQUEST["course_pw"] : null);
        break;
      case "leave":
        $course = $this->checkCourse($ref_id);
        echo $this->leaveCourse($course);
        break;
      case "sync":
        $this->printCurrentDesk();
        break;
      case "search":
        $this->searchMagazin(isset($_REQUEST["group_by_dozent"]) ? (bool) $_REQUEST["group_by_dozent"] : false);
        break;
      case "magazin":
        $this->printMagazin();
        break;
      default:
        echo "ACTION_UNKNOWN";
        break;
    }

  }

  function checkCourse($ref_id) {

    $factory = new IlObjectFactory();
    $course = $factory->getInstanceByRefId($ref_id);

    if(strcmp($course->getType(), "crs") !== 0) {
      die("not a course object");
    } else {
      return $course;
    }

  }

  function leaveCourse($course) {
    global $ilUser;

    if(!$course->getMembersObject()->isMember($ilUser->getId()) // user not in course
      || $course->getMembersObject()->isLastAdmin($ilUser->getId())) // user is last admin
      return "PERMISSION_DENIED";

    $course->getMembersObject()->delete($ilUser->getId());
    $course->getMembersObject()->sendUnsubscribeNotificationToAdmins($ilUser->getId());
    $course->getMembersObject()->sendNotification($course->getMembersObject()->NOTIFY_UNSUBSCRIBE, $ilUser->getId());

    include_once './Modules/Forum/classes/class.ilForumNotification.php';
    ilForumNotification::checkForumsExistsDelete($course->ref_id, $ilUser->getId());
  }

  function joinCourse($course, $password=NULL) {
    global $ilUser, $ilObjDataCache;

    if($ilUser->getId() == ANONYMOUS_USER_ID         || // anonymous user not allowed to subscribe
       $course->getSubScriptionLimitationType() == 0 || // subscription deactivated
      !$course->inSubScriptionTime()) // subscription time limit exceeded
          return "PERMISSION_DENIED";

    if($course->getMembersObject()->isMember($ilUser->getId()))
        return "ALREADY_SUBSCRIBED";

    switch($course->getSubScriptionType()) { // check for deactivated registration && valid password
      case $course->SUBSCRIPTION_DEACTIVATED:
        return "SUBSCRIPTION_DEACTIVATED";
      case $course->SUBSCRIPTION_PASSWORD:
        if($password == NULL)
          return "PASSWORD_NEEDED";
        else if(strcmp($course->getSubScriptionPassword(), $password) !== 0)
          return "WRONG_PASSWORD";
    }

    include_once('./Modules/Course/classes/class.ilCourseWaitingList.php');
    include_once('./Modules/Course/classes/class.ilCourseParticipants.php');

    $obj_id       = $ilObjDataCache->lookupObjId($course->ref_id);
    $participants = ilCourseParticipants::_getInstanceByObjId($obj_id);
    $free         = max(0, $course->getSubscriptionMaxMembers() - $participants->getCountMembers());
    $waiting_list = new ilCourseWaitingList($course->getId());

    if($waiting_list->isOnList($ilUser->getId())) {
      return "ON_WAITINGLIST";
    }

    // TODO: Check if course is full && no waitinglist enabled
    if($course->isSubscriptionMembershipLimited() and $course->enabledWaitingList() and (!$free or $waiting_list->getCountUsers())) {
      $waiting_list->addToList($ilUser->getId());
      $participants->sendNotification($participants->NOTIFY_SUBSCRIPTION_REQUEST, $ilUser->getId());
      $participants->sendNotification($participants->NOTIFY_WAITING_LIST, $ilUser->getId());
    }

    switch($course->getSubScriptionType()) {
      case $course->SUBSCRIPTION_CONFIRMATION:
        if(in_array($ilUser->getId(), $participants->getSubscribers())) // user already sent subscription request
          return "WAITING_FOR_CONFIRMATION";
        $participants->addSubscriber($ilUser->getId());
        $participants->updateSubscriptionTime($ilUser->getId(), time());
        $participants->updateSubject($ilUser->getId(), ilUtil::stripSlashes($_POST['subject']));
        $participants->sendNotification($participants->NOTIFY_SUBSCRIPTION_REQUEST, $ilUser->getId());
        return "JOIN_REQUEST_SENT";
      default:
        $participants->add($ilUser->getId(), IL_CRS_MEMBER);
        $participants->sendNotification($participants->NOTIFY_ADMINS, $ilUser->getId());
        $participants->sendNotification($participants->NOTIFY_REGISTERED, $ilUser->getId());
        include_once './Modules/Forum/classes/class.ilForumNotification.php';
        ilForumNotification::checkForumsExistsInsert($course->getRefId(), $ilUser->getId());
        return "JOINED";
    }
  }

  function printCurrentDesk() {
    global $ilUser;

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
    $items   = $desktop->addChild("Items");

    ## PERSOENLICHER SCHREIBTISCH AUSGEBEN
    $desktop_items = $ilUser->getDesktopItems();

    ## In $desktop_items sind nun ILIAS Objekte.
    foreach($desktop_items as $item) {
      $this->desktopItem2Xml($item,$items,$notifications);
    }

    ## AUSGABE DER XML STRUKTUR
    echo $xml->asXML();
  }

  function printMagazin() {
    global $ilUser, $ilAccess;

    ## Header auf XML stellen
    header ("Content-Type:text/xml");

    ## MAIN XML TAG (auch XML Root genannt)
    $xml = new SimpleXMLElement("<Iliconnect/>");

    ## XML ILICONNECT CURRENT
    $current = $xml->addChild("Current");

    ## XML ILICONNECT CURRENT NOTIFICATIONS
    $notifications = $current->addChild("Notifications");

    ## XML ILICONNECT CURRENT DESKTOP
    $desktop = $current->addChild("Magazin");

    ## Tree ist eine ILIAS eigene Variable
    global $tree;
    $desktop_items= $tree->getChilds(1);

    ## In $desktop_items sind nun ILIAS Objekte.
    foreach($desktop_items as $item) {
      if($ilAccess->checkAccess("read", "show", $item["ref_id"], $item["type"], $item["obj_id"]))
        $this->desktopItem2Xml($item,$desktop, new SimpleXMLElement("<dontshow/>"));
    }

    ## AUSGABE DER XML STRUKTUR
    echo $xml->asXML();
  }

  function getAllCourses(&$courses, $node_id=1) {
      global $tree;

      $courses_iter = $tree->getChildsByType($node_id, "crs");
      foreach($courses_iter as &$course) {
          $courses[] = $course;
      }   

      $childs = $tree->getChilds($node_id);
      foreach($childs as &$child) {
          $this->getAllCourses($courses, $child["ref_id"]);
      }   

  }   

  function searchMagazin($group_by_dozent=false) {
    global $ilUser, $tree, $ilAccess;

    $xml = new SimpleXMLElement("<Iliconnect/>");

    $current = $xml->addChild("Current");
    
    $result_xml = $current->addChild("Results");
    $results = array();

    $needle = $_REQUEST['searchfor'];
    $courses = array();
    $this->getAllCourses($courses);
    foreach($courses as &$course) {
        $owner = ilObjectFactory::getInstanceByObjId($course["owner"]);
        if((stristr($course["title"], $needle) !== false ||
            stristr($owner->getFullName(), $needle) !== false) &&
            $ilAccess->checkAccess("visible", "show", $course["ref_id"], "crs", $course["obj_id"])) {
            if(!is_array($results[$owner->getFullName()])) /* store course results as key (dozent), value (courses) pairs  */
                $results[$owner->getFullName()] = array();
            $results[$owner->getFullName()][] = $course;
        }
    }

    foreach($results as $dozent => &$courses) {
        $course_xml = $group_by_dozent ? $result_xml->addChild("Dozent") : $result_xml;
        foreach($courses as &$course) {
            $item = $course_xml->addChild("Item");
            foreach(array("title", "description", "ref_id", "type") as $attribute)
                $item->addChild($attribute, $course[$attribute]);
            $item->addChild("owner", $dozent);
        }
    }

    ## AUSGABE DER XML STRUKTUR
    header ("Content-Type:text/xml");
    echo $xml->asXML();
  }

  ## Der (un)uebersichtlichkeit halber in eine eigene funktion gepackt.
  ## erzeugt einen eintrag innerhalb des NOTIFICATIONS tag
  function notification2Xml($a,$nxml)
  {

    $factory = new IlObjectFactory();

    if($a["type"] == "exc") { /* make a notification for each assignment of an exercise */
       include_once("./Modules/Exercise/classes/class.ilExAssignment.php");
       $exc = $factory->getInstanceByRefId($a["ref_id"]);
       $assignments = ilExAssignment::getAssignmentDataOfExercise($exc->getId());
       $cnt=0;
       foreach($assignments as &$assignment) {
           $cnt+=1;
           if($assignment["deadline"] > time()) { /* make sure the deadline has not been reached yet */
               $notify = $nxml->addChild("Notification");
               $notify->addChild("title", $a["title"]);
               $notify->addChild("ref_id", $a["ref_id"]."-".$cnt);
               $notify->addChild("description", $assignment["title"]);
               $notify->addChild("date", $assignment["deadline"]);
           }
        }
    } else if($a["type"] == "tst") {
        $tst = $factory->getInstanceByRefId($a["ref_id"]);
        $date_arr = date_parse_from_format("YmdHis", $tst->getEndingTime()); /* convert to unix timestamp */
        $deadline = mktime($date_arr["hour"], $date_arr["minute"], $date_arr["seconds"],
            $date_arr["month"], $date_arr["day"], $date_arr["year"]);
        if($deadline > time()) {
            $notify = $nxml->addChild("Notification");
            $notify->addChild("title", $a["title"]);
            $notify->addChild("ref_id", $a["ref_id"]);
            $notify->addChild("description", $a["description"]);
            $notify->addChild("date", $deadline);
        }
    }

  }

  ## FUNCTION FUER DIE REKURSIVE ABARBEITUNG DER CHILD ITEMS
  function desktopItem2Xml($array,$sxml,$notifications){
    global $ilAccess, $ilUser;

    if(strstr("file|fold|crs|tst|exc",$array["type"]))
    {
      $item = $sxml->addChild("Item");
      $item->addChild("title", $array["title"]);
      $item->addChild("description", $array["description"]);
      $item->addChild("type", $array["type"]);
      $item->addChild("ref_id", $array["ref_id"]);
      if($array["last_update"]) {
        $date = new DateTime($array["last_update"]);
        $date->format("U");
        $item->addChild("last_update", $date->getTimestamp());
      } else {
        $item->addChild("last_update");
      }
      
      $state = ilChangeEvent::_lookupInsideChangeState($array["obj_id"], $ilUser->getId());
      $state2 = ilChangeEvent::_lookupChangeState($array["obj_id"], $ilUser->getId());
      
      if(($state + $state2) > 0 && strstr("file|fold|crs|exc",$array["type"])) {
        $item->addChild("changed", "true");
      } else {
        $item->addChild("changed", "false");
      }
      
      if($array["type"] == "exc" || $array["type"] == "tst")
      {
        $this->notification2Xml($array, $notifications);
      }

      global $tree;
      $children=$tree->getChilds($array["ref_id"]);
      if(count($children) > 0) {
        $subitems = $item->addChild("Items");
        foreach($children as $child)
        {
	  //if($ilAccess->checkAccess("read", "show", $children["ref_id"], $children["type"], $children["obj_id"]) ||
	    //($ilAccess->checkAccess("write", "show", $children["ref_id"], $children["type"], $children["obj_id"])) ||
	    //($ilAccess->checkAccess("visible", "show", $children["ref_id"], $children["type"], $children["obj_id"])) ||
	    //($ilAccess->checkAccess("edit_permission", "show", $children["ref_id"], $children["type"], $children["obj_id"])))
            $this->desktopItem2Xml($child, $subitems, $notifications);
        }
      }
    }
  }

}

?>

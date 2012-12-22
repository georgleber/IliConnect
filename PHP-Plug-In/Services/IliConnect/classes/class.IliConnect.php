<?php

class IliConnect{
  
  function __construct(){

    $ref_id = intval($_GET['ref_id']);

    switch($_GET["action"]) {
      case "join":
        $course = $this->checkCourse($ref_id);
        echo $this->joinCourse($course, isset($_GET["course_pw"]) ? $_GET["course_pw"] : null);
        break;
      case "leave":
        $course = $this->checkCourse($ref_id);
        echo $this->leaveCourse($course);
        break;
      case "sync":
        $this->printCurrentDesk();
        break;
      case "search":
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
    global $ilUser;
    global $ilObjDataCache;

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

    ## PERSOENLICHER SCHREIBTISCH AUSGEBEN
    $desktop_items = $ilUser->getDesktopItems();

    ## In $desktop_items sind nun ILIAS Objekte.
    foreach($desktop_items as $item) {
      $this->desktopItem2Xml($item,$desktop,$notifications);
    }

    ## AUSGABE DER XML STRUKTUR
    echo $xml->asXML();

  }

  function printMagazin() {

		global $ilUser

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

    ## Tree ist eine ILIAS eigene Variable
    global $tree;
    $desktop_items= $tree->getChilds(1);

    ## In $desktop_items sind nun ILIAS Objekte.
    foreach($desktop_items as $item) {
      $this->desktopItem2Xml($item,$desktop, new SimpleXMLElement("<dontshow/>"));
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
        $this->notification2Xml($array,$notifications);
      }

      global $tree;
      $children=$tree->getChilds($array[ref_id]);
      foreach($children as $child)
      {
        $this->desktopItem2Xml($child,$item,$notifications);
      }
    } else {
      # Debugging
      #print_r($array);
    }

  }

}

?>

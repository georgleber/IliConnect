<?php

class IliConnect{
	require_once('classes/class.ilObjectFactory.php');
	require_once('Services/Utilities/classes/class.ilUtil.php');
	
	function __construct(){

		$ref_id = intval($_GET['ref_id']);

		switch($_GET["action"]) {
			case "join":
				if(checkCourse($ref_id) {
					$factory = new IlObjectFactory();
					$course = $factory->getInstanceByRefId($ref_id);
					echo joinCourse($course, isset($_GET["course_pw"]) ? $_GET["course_pw"] : null);
				}
				break;
			case "leave":
				if(checkCourse($ref_id) {
					$factory = new IlObjectFactory();
					$course = $factory->getInstanceByRefId($ref_id);
					echo leaveCourse($course);
				}
				break;
			case "sync":
				break;
			case "search":
				break;
			case "magazin":
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
			return true;
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

		$obj_id 			= $ilObjDataCache->lookupObjId($course->ref_id);
		$participants = ilCourseParticipants::_getInstanceByObjId($obj_id);
    $free 				= max(0, $course->getSubscriptionMaxMembers() - $participants->getCountMembers());
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

	
}

?>

<?php

class IliConnect{
	
	function __construct(){

		switch($_GET["action"]) {
			case "join":
				echo joinCourse($course, isset($_GET["course_pw"]) ? $_GET["course_pw"] : null);
				break;
			case "leave":
				echo leaveCourse($course);
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
	
}

?>

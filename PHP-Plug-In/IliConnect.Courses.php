<?php

## Die POST Variablen muessen gesetzt sein, damit die ilAuthFactory Klasse die Authentifizierung gegen ILIAS macht.
## Zum debuggen im Browser, habe ich diese beiden Zeilen hinzugefuegt.
$_POST['username'] = 'lukas'; #$_REQUEST['user'];
$_POST['password'] = 'foobar'; #$_REQUEST['pass'];

include_once 'Services/Authentication/classes/class.ilAuthFactory.php';
ilAuthFactory::setContext(ilAuthFactory::CONTEXT_SOAP);

require_once("Services/Init/classes/class.ilInitialisation.php");
$ilInit = new ilInitialisation();
$ilInit->initILIAS('webdav');

$login = $ilUser->getLogin();

require_once('classes/class.ilObjectFactory.php');
require_once('Services/Utilities/classes/class.ilUtil.php');

$ref_id = intval($_GET['ref_id']);
$factory = new IlObjectFactory();
$course = $factory->getInstanceByRefId($ref_id);

if(strcmp($course->getType(), "crs") !== 0) {
	die("not a course object");
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

	if($ilUser->getId() == ANONYMOUS_USER_ID || // anonymous user not allowed to subscribe
		$course->getSubScriptionLimitationType() == 0 || // subscription deactivated
		!$course->inSubScriptionTime() || // subscription time limit exceeded
		$course->getMembersObject()->isMember($ilUser->getId())) // user already subscribed
		return "PERMISSION_DENIED";

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

	$obj_id = $ilObjDataCache->lookupObjId($course->ref_id);
	$participants = ilCourseParticipants::_getInstanceByObjId($obj_id);
        $free = max(0, $course->getSubscriptionMaxMembers() - $participants->getCountMembers());
        $waiting_list = new ilCourseWaitingList($course->getId());

	// TODO: Check if course is full && no waitinglist enabled
	if($course->isSubscriptionMembershipLimited() and $course->enabledWaitingList() and (!$free or $waiting_list->getCountUsers())) {
		$waiting_list->addToList($ilUser->getId());
		$participants->sendNotification($participants->NOTIFY_SUBSCRIPTION_REQUEST, $ilUser->getId());
		$participants->sendNotification($participants->NOTIFY_WAITING_LIST, $ilUser->getId());
	}

	switch($course->getSubScriptionType()) {
		case $course->SUBSCRIPTION_CONFIRMATION:
			$participants->addSubscriber($ilUser->getId());
			$participants->updateSubscriptionTime($ilUser->getId(), time());
			$participants->updateSubject($ilUser->getId(), ilUtil::stripSlashes($_POST['subject']));
			$participants->sendNotification($participants->NOTIFY_SUBSCRIPTION_REQUEST, $ilUser->getId());
			break;
		default:
			$participants->add($ilUser->getId(), IL_CRS_MEMBER);
			$participants->sendNotification($participants->NOTIFY_ADMINS, $ilUser->getId());
			$participants->sendNotification($participants->NOTIFY_REGISTERED, $ilUser->getId());
			include_once './Modules/Forum/classes/class.ilForumNotification.php';
			ilForumNotification::checkForumsExistsInsert($course->getRefId(), $ilUser->getId());
			break;
	}

}

switch($_GET["action"]) {
	case "join":
		echo joinCourse($course, isset($_GET["course_pw"]) ? $_GET["course_pw"] : null);
		break;
	case "leave":
		echo leaveCourse($course);
		break;
	default:
		echo "ACTION_UNKNOWN";
		break;
}

?>

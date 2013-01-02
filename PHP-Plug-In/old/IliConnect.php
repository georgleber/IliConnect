<?php

define('IliConnect', true);

/*foreach(array_keys($_COOKIE) as $cookiename) {
	echo $cookiename;
	setcookie($cookiename);
	unset($_COOKIE[$cookiename]);
}*/

switch($_GET["action"]) {
	case "sync":
  case "magazin":
		include("IliConnect.Sync.php");
		break;
	case "join":
	case "leave":
		include("IliConnect.Courses.php");
		break;
	default:
		echo "ACTION_UNKNOWN";
		break;
}

?>

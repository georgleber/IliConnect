<?php

switch($_GET["action"]) {
	case "sync":
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

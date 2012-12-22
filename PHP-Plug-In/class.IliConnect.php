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
	
}

?>

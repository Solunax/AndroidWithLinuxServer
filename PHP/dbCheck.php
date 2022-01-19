<?php
	$host = 'localhost';
	$user = 'test';
	$pw = '1111';
	$dbName = 'USER';
	$con = new mysqli($host, $user, $pw, $dbName);

	if(!$con){
		echo "FAIL";	
	}
?>

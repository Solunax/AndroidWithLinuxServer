<?php
	$host = 'localhost';
	$user = 'test';
	$pw = '1111';
	$dbName = 'USER';
	$mysqli = new mysqli($host, $user, $pw, $dbName);

	if($mysqli){
		echo "GOOD";
	}else{
		echo "FAIL";	
	}
?>

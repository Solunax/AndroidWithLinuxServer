<?php
	include 'dbCheck.php';
	
	$id = $_GET['id'];

	mysqli_set_charset($con, "utf8");
	$res = mysqli_query($con, "select id, password from USER_INFO where id ='".$id."';");
	$result = array();

	while($row = mysqli_fetch_array($res)){
		array_push($result, $row['id'], $row['password']);
	}

	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("data"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;

	mysqli_close($con);
?>

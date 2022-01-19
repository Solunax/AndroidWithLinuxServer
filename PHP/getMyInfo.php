<?php
	include 'dbCheck.php';
	
	$id = $_GET['id'];

	mysqli_set_charset($con, "utf8");
	$res = mysqli_query($con, "select id, name, auth from USER_INFO where id ='".$id."';");
	$res1 = mysqli_query($con, "select path from USER_IMAGE where id ='".$id."';");
	$result = array();


	$row = mysqli_fetch_array($res);
	array_push($result, $row['id'], $row['name'], $row['auth']);

	$row1 = mysqli_fetch_array($res1);
	array_push($result, $row1['path']);

	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("data"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;

	mysqli_close($con);
?>

<?php
	include 'dbCheck.php';
	
	mysqli_set_charset($con, "utf8");
	$res = mysqli_query($con, "select * from USER_INFO;");
	$result = array();

	while($row = mysqli_fetch_array($res)){
		array_push($result, array('id'=>$row[0], 'name'=>$row[1]));
	}

	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("data"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;

	mysqli_close($con);
?>

<?php
	include 'dbCheck.php';
	
	mysqli_set_charset($con, "utf8");
	$res = mysqli_query($con, "select id from USER_INFO;");
	$result = array();

	if($res){
		while($row = mysqli_fetch_array($res)){
			array_push($result, $row[0]);
		}

		header('Content-Type: application/json; charset=utf8');
		$json = json_encode(array("data"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
		echo $json;
	}

	mysqli_close($con);
?>

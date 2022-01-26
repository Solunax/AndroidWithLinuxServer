<?php
	include 'dbCheck.php';
	
	mysqli_set_charset($con, "utf8");

	$id = $_GET['id'];

	$result = array();
	$res = mysqli_query($con, "delete from USER_IMAGE where id = '".$id."';");
	if($res){
		$res1 = mysqli_query($con, "delete from USER_INFO where id = '".$id."';");
		if($res1){
			$res2 = mysqli_query($con, "select id, name from USER_INFO;");

			while($row = mysqli_fetch_array($res2)){
				array_push($result, array('id'=>$row[0], 'name'=>$row[1]));
			}

			header('Content-Type: application/json; charset=utf8');
			$json = json_encode(array("data"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
			echo $json;
		}

	}else{
		echo "Fail";
	}

	mysqli_close($con);
?>

<?php
	include 'dbCheck.php';
	
	mysqli_set_charset($con, "utf8");

	$id = $_POST['id'];
	$name = $_POST['name'];

	$result = array();
	$data = "'".$id."','".$name."'";
	$res = mysqli_query($con, "insert into USER_INFO values(".$data.")");

	if($res){
		$res1 = mysqli_query($con, "select * from USER_INFO where id = '".$id."';");
		while($row = mysqli_fetch_array($res1)){
			array_push($result, array('id'=>$row[0], 'name'=>$row[1]));
		}

		header('Content-Type: application/json; charset=utf8');
		$json = json_encode(array("data"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
		echo $json;
	}else{
		echo "Fail";
	}


	mysqli_close($con);
?>

<?php
	$con = mysqli_connect("localhost", "test", "1111", "USER");
	if(!$con){
		echo "FAIL";	
	}
	
	mysqli_set_charset($con, "utf8");

	$id = $_POST['id'];
	$name = $_POST['name'];

	if(empty($id)){
		$errMSG = "오류! 아이디를 입력하세요";
	}
	if(empty($name)){
		$errMSG = "오류! 이름을 입력하세요";
	}

	if(!isset($errMSG)){
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
	}else{
		echo $errMSG;
	}

	mysqli_close($con);
?>

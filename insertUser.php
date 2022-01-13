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
		$data = "'".$id."','".$name."'";
		$res = mysqli_query($con, "insert into USER_INFO values(".$data.")");

		if($res)
			echo "Success!";
		else
			echo "Fail";
	}else{
		echo $errMSG;
	}


	mysqli_close($con);
?>

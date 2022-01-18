<?php
	include 'dbCheck.php';

	mysqli_set_charset($con, "utf8");

	$id = $_POST['id'];
	$serverPath = $_POST['serverPath'];

	$file_path = "userImage/";
	$var = $_POST['result'];
	$file_path = $file_path.basename($_FILES['uploaded_file']['name']);
	
	if(empty($id)){
		$errMSG = "오류! 아이디를 입력하세요";
	}

	if(empty($serverPath)){
		$errMSG = "오류! 경로를 입력하세요";
	}

	if(!isset($errMSG)){
		$id = str_replace("\"","", $id);
		$serverPath = str_replace("\"","", $serverPath);

		$result = array();
		$data = "'".$id."','".$serverPath."'";
		echo $id;
		echo $data;
		$res = mysqli_query($con, "insert into USER_IMAGE values(".$data.");");

		if($res){
			$res1 = mysqli_query($con, "select * from USER_IMAGE where id = '".$id."';");			
			while($row = mysqli_fetch_array($res1)){
				array_push($result, array('id'=>$row[0], 'file_path'=>$row[1]));
			}

			if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)){
				header('Content-Type: application/json; charset=utf8');
				$json = json_encode(array("data"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
				echo $json;
			}else{
				echo "FAIL";
			}
		}else{
			$result = array("result" => "ERROR");
		}
	}else{
		echo $errMSG;
	}

?>

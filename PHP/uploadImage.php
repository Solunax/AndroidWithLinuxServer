<?php
	include 'dbCheck.php';

	mysqli_set_charset($con, "utf8");

	$id = $_POST['id'];
	$serverPath = $_POST['serverPath'];

	$file_path = "userImage/";
	$var = $_POST['result'];
	$file_path = $file_path.basename($_FILES['uploaded_file']['name']);
	
	$id = str_replace("\"","", $id);
	$serverPath = str_replace("\"","", $serverPath);

	$result = array();
	$data = "'".$id."','".$serverPath."'";

	$res = mysqli_query($con, "insert into USER_IMAGE values(".$data.") on duplicate key update id ='".$id."', path ='".$serverPath."';");

	if($res){
		$res1 = mysqli_query($con, "select id, path from USER_IMAGE where id = '".$id."';");
		$row1 = mysqli_fetch_array($res1);
		array_push($result, $row1['id'], $row1['path']);			

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


?>

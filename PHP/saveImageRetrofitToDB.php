<?php
 header('Content-Type: bitmap; charset=utf-8');
 
 if(isset($_POST["encoded_string"])){
 	
	$encoded_string = $_POST["encoded_string"];
	$image_name = $_POST["image_name"];
	
	$decoded_string = base64_decode($encoded_string);
	
	$path = 'Images/'.$image_name;
	
	$file = fopen($path, 'wb');
	
	$is_written = fwrite($file, $decoded_string);
	fclose($file);
	
	if($is_written > 0) {

		$connection = mysqli_connect("localhost","id5266469_psycho","123456789","id5266469_psychodb");
		if (mysqli_connect_errno($connection))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

		$query = "INSERT INTO testFCM (TOKEN,Email) values('$path','$image_name');";
		
		$result = mysqli_query($connection, $query) ;
		
		if($result){
		     $key['result'] = "success";
	echo json_encode($key);
		
		}else{
				     $key['result'] = "Faild";
	echo json_encode($key);
		}
		mysqli_close($connection);
	}
 }
?>
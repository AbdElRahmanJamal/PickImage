<?php
header('Content-Type: bitmap; charset=utf-8');
$data = file_get_contents('php://input');
$json_data = json_decode($data , true);
if ($_SERVER['REQUEST_METHOD'] === 'POST') 
{
//code to process data
if ($data == "" || empty($json_data['encoded_string']) || empty($json_data['image_name']))
{
    $key['result'] = 'Invalid Values';
    
}
else
{
    $encoded_string = $json_data['encoded_string'];
	$image_name = $json_data['image_name'];
	
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
}
?>
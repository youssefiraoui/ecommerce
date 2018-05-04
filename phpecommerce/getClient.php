<?php

if($_SERVER["REQUEST_METHOD"] == "POST") {
    
    include 'connection.php';
    $login = $_POST["login"];
    $password = $_POST["password"];
    getClient($login, $password);
}

function getClient($login, $password) {
    
    global $connect;
    
    $query = " select * from client where loginclient='$login' and mdpclient='$password' ";
    
    $result = mysqli_query($connect, $query);
    $number_of_rows = mysqli_num_rows($result);
    
    $temp_array = array();
    
    if($number_of_rows > 0) {
        while ($row = mysqli_fetch_assoc($result)) {
            $temp_array[] = $row;
        }
    }
    
    header('Content-Type: application\json');
    echo json_encode(array("client"=>$temp_array));
    mysqli_close($connect);
    
}
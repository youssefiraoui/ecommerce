<?php

if($_SERVER["REQUEST_METHOD"] == "POST") {
    
    include 'connection.php';  
    $selectedItem = $_POST['selectedItem'];
    getSpecificProducts($selectedItem);
}

function getSpecificProducts($selectedItem) {
    
    global $connect;
    
    $query = " select * from produit inner join catalogue on produit.idcatalogue = catalogue.idcatalogue where nomcatalogue = '$selectedItem' ";
    
    $result = mysqli_query($connect, $query);
    $number_of_rows = mysqli_num_rows($result);
    
    $temp_array = array();
    
    if($number_of_rows >0) {
        while($row = mysqli_fetch_assoc($result)) {
            $temp_array[] = $row;
        }
    }
    
    header('Content-Type: application\json');
    echo json_encode(array("specificprod"=>$temp_array));
    mysqli_close($connect);
    
}
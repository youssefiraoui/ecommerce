<?php

if($_SERVER["REQUEST_METHOD"] == "POST"){
    require 'connection.php';
    addClient();
}

function addClient() {
    
    global $connect;
    
    $nom = $_POST["nom"];
    $email = "elmehdiAssimeddine@gmail.com";
    $login = $_POST["login"];
    $password = $_POST["password"];    
  
    $query = " insert into client (nomclient, telclient, loginclient, mdpclient, datenaissance, email) VALUES ('$nom', '$tel', '$login', '$password', '$dateNaissance', '$email') ";
    
    mysqli_query($connect, $query) or die (mysqli_errno($connect));
    mysqli_close($connect);
    
}




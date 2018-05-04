<?php
if($_SERVER["REQUEST_METHOD"] == "POST") {

    include 'connection.php';
    addCommande();
}

function addCommande() {

    global $connect;

    $idClient = $_POST["idClient"];
    $dateCommande = $_POST["dateCommande"];

    $query = " insert into commande (idClient, dateCommande) VALUES ('$idClient', '$dateCommande')";

    mysqli_query($connect, $query) or die (mysqli_errno($connect));
    mysqli_close($connect);

}
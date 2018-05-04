<?php
if($_SERVER["REQUEST_METHOD"] == "POST") {

    include 'connection.php';
    addLigneCommande();
}

function addLigneCommande() {

    global $connect;

    $dateCommande = $_POST['dateCommande'];

    $idproduit = $_POST['idproduit'];
    $Qtecomm = $_POST['Qtecomm'];
    $idcommande = 1;

    $query1 = " select idcommande from commande where datecommande = '$dateCommande' ";
    $result = $connect->query($query1);

    if($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $idcommande = $row['idcommande'];
        }
    }

    $query = " insert into ligne_de_commande (idcommande, idproduit, Qtecomm) VALUES
                ('$idcommande', '$idproduit', '$Qtecomm')";

    mysqli_query($connect, $query) or die (mysqli_errno($connect));
    mysqli_close($connect);

}
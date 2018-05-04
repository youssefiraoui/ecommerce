package com.example.laptop.entities;

public class Produit {

    private String idProduit;
    private String nomProduit;
    private String prix;

    public Produit() {

    }

    public String getIdProduit() {
        return idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public String getPrix() {
        return prix;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}

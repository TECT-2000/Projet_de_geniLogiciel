package com.example.miketsebo.projetgenielogiciel.Model.principal;

public class Contact {
    private long id;
    private String nomContact;
    private int numero;

    public Contact(long id, String nomContact, int numero) {
        this.id = id;
        this.nomContact = nomContact;
        this.numero = numero;
    }

    public Contact() {
    }

    public Contact(String nomContact, int numero) {
        this.nomContact = nomContact;
        this.numero = numero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomContact() {
        return nomContact;
    }

    public void setNomContact(String nomContact) {
        this.nomContact = nomContact;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "nomContact='" + nomContact + '\'' +
                ", numero=" + numero +
                '}';
    }
}

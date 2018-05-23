package com.example.miketsebo.projetgenielogiciel.Model.principal;

import java.util.ArrayList;

public class Groupe {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String nomGroupe;
    ArrayList<Contact> contacts;

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public Groupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public Groupe(String nomGroupe, ArrayList<Contact> contacts) {
        this.nomGroupe = nomGroupe;
        this.contacts = contacts;
    }

    public Groupe(int id, String nomGroupe, ArrayList<Contact> contacts) {
        this.id = id;
        this.nomGroupe = nomGroupe;
        this.contacts = contacts;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    @Override
    public String toString() {
        return "Groupe{" +
                "nomGroupe='" + nomGroupe + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}

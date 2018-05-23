package com.example.miketsebo.projetgenielogiciel.Model.principal;

import java.sql.Date;

public class Message {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String contenu;
    private Date dateEnvoi;
    private Date dateModification;
    private String titre;

    public Message(String contenu, Date dateEnvoi, Date dateModification, String titre) {
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.dateModification = dateModification;
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "Message{" +
                "contenu='" + contenu + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", dateModification=" + dateModification +
                ", titre='" + titre + '\'' +
                '}';
    }
}

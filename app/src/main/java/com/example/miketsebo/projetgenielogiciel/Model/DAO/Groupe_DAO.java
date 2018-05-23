package com.example.miketsebo.projetgenielogiciel.Model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.miketsebo.projetgenielogiciel.Model.BD.ConnexionBD;
import com.example.miketsebo.projetgenielogiciel.Model.principal.Contact;
import com.example.miketsebo.projetgenielogiciel.Model.principal.Groupe;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/*
     A MODIFIER
 */

public class Groupe_DAO extends DAOBase{
    //méthodes pour communiquer avec la table Groupe
    Contact_DAO contactDao;
    /*
     *créer un groupe avec contacts
     */
    public long enregisterGroupe(Groupe groupe){
        SQLiteDatabase db= this.conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(ConnexionBD.getNomGroupe(),groupe.getNomGroupe());

        //insertion dans la BD
        long groupe_res=db.insert(ConnexionBD.getTableGroupe(),null,values);

        entregistrerContactsGroupe(groupe.getContacts(),(int)groupe_res);

        this.closeDB();
        return groupe_res;
    }

    /*
     *récupérer un groupe de la bd à partir de son identifiant
     */
    public Groupe selectionnerGroupeByID(int groupe_id){
        SQLiteDatabase db=this.conn.getReadableDatabase();

        String query="SELECT * FROM "+ConnexionBD.getTableGroupe()+" WHERE "+ConnexionBD.getKeyId()+" = "+groupe_id;

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        if(c !=null)
            c.moveToFirst();

        ArrayList<Contact> contacts;
        contacts=contactDao.selectionnerContactsGroupe(groupe_id);

        Groupe groupe=new Groupe(groupe_id,c.getString(c.getColumnIndex(ConnexionBD.getNomGroupe())),contacts);

        c.close();
        return groupe;
    }

    /*
     *récupérer l'identifiant d'un groupe de la bd à partir de son nom
     */
    public int selectionnerIdGroupe(String nomGroupe){
        SQLiteDatabase db=this.conn.getReadableDatabase();

        String query="SELECT * FROM "+ConnexionBD.getTableGroupe()+" WHERE "+ConnexionBD.getNomGroupe()+" = '"+nomGroupe+"'";

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        if(c !=null)
            c.moveToFirst();

        int id= c.getInt(c.getColumnIndex(ConnexionBD.getKeyId()));
        c.close();
        return id;
    }
    /*
     *modifier un contact de la bd
     */
    public int modifierNomGroupe(String ancienNom,String nouveauNom) {
        SQLiteDatabase db=this.open();

        ContentValues values=new ContentValues();
        values.put(ConnexionBD.getNomGroupe(),nouveauNom);

        //mise à jour de l'enregistrement
        return db.update(ConnexionBD.getTableGroupe(),values,ConnexionBD.getKeyId()+" = ?",
                new String[]{String.valueOf(selectionnerIdGroupe(ancienNom))});
    }
    /*
        Ajouter un membre à un groupe
     */
    public boolean ajouterContactGroupe(Contact contact,String nomGroupe){
        SQLiteDatabase db=this.conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(ConnexionBD.getGcIdContact(),contact.getId());
        values.put(ConnexionBD.getGcIdGroupe(),selectionnerIdGroupe(nomGroupe));

        long resp=db.insert(ConnexionBD.getTableGroupeContact(),null,values);

        this.closeDB();;
        return (resp!=0);
    }
    /*
        supprimer un membre à un groupe
     */
    public boolean supprimerContactGroupe(Contact contact,String nomGroupe){
        SQLiteDatabase db=this.conn.getWritableDatabase();

        long resp=db.delete(ConnexionBD.getTableGroupeContact(),ConnexionBD.getGcIdContact()+" = ?",
        new String[]{String.valueOf(contactDao.selectionnerIDContactByNumber(contact))});

        this.closeDB();
        return (resp!=0);
    }
    /*
     *supprimer un contact de la bd à partir de son identifiant
     */
    public int supprimerGroupe(String nomGroupe){
        SQLiteDatabase db=this.open();
        int idgroupe=selectionnerIdGroupe(nomGroupe);
        System.out.println(idgroupe);

        db.delete(ConnexionBD.getTableGroupeContact(),ConnexionBD.getGcIdGroupe()+" = ?",new String[]{String.valueOf(idgroupe)});

        return db.delete(ConnexionBD.getTableGroupe(),ConnexionBD.getKeyId()+" = ?",
                new String []{String.valueOf(idgroupe)});
    }

    public int entregistrerContactsGroupe(ArrayList<Contact> contacts, int idGroupe){

        SQLiteDatabase db= this.conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        for(Contact c: contacts) {
            values.put(ConnexionBD.getGcIdContact(), contactDao.selectionnerIDContactByNumber(c));
            values.put(ConnexionBD.getGcIdGroupe(), idGroupe);

            //insertion dans la BD
            long contact_res = db.insert(ConnexionBD.getTableGroupeContact(), null, values);
        }
        this.closeDB();

        //faut encore vérifier que le contact n'existe pas dans la bd
        return 0;
    }

    public Groupe_DAO(Context context) {
        super(context);
    contactDao=new Contact_DAO(context);
    }
}

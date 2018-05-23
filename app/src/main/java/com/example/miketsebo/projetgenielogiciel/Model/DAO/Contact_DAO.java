package com.example.miketsebo.projetgenielogiciel.Model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.miketsebo.projetgenielogiciel.Model.BD.ConnexionBD;
import com.example.miketsebo.projetgenielogiciel.Model.principal.Contact;

import java.util.ArrayList;
import java.util.List;

public class Contact_DAO extends DAOBase{

    /*
    *créer un contact
     */
    public long enregisterContact(Contact contact){
        SQLiteDatabase db= this.open();

             ContentValues values = new ContentValues();
            values.put(ConnexionBD.getNomContact(), contact.getNomContact());
            values.put(ConnexionBD.getNumeroContact(), contact.getNumero());

            //insertion dans la BD
            long contact_res = db.insert(ConnexionBD.getTableContact(), null, values);

            this.closeDB();
            return contact_res;

        //faut encore vérifier que le contact n'existe pas dans la bd

    }

    /*
     *récupérer un contact de la bd à partir de son identifiant
     */
    public Contact selectionnerContact(long contact_id){
        SQLiteDatabase db=this.conn.getReadableDatabase();

        String query="SELECT * FROM "+ConnexionBD.getTableContact()+" WHERE "+ConnexionBD.getKeyId()+" = "+contact_id;

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        if(c !=null)
            c.moveToFirst();

        Contact contact=new Contact(c.getInt(c.getColumnIndex(ConnexionBD.getKeyId())),
                c.getString(c.getColumnIndex(ConnexionBD.getNomContact())),
                c.getInt(c.getColumnIndex(ConnexionBD.getNumeroContact())));
        this.closeDB();
        return contact;
    }
    /*
     *récupérer l' ID contact de la bd à partir de son numero
     */
    public int selectionnerIDContactByNumber(Contact contact){
        SQLiteDatabase db=this.conn.getReadableDatabase();

        String query="SELECT id FROM "+ConnexionBD.getTableContact()+" WHERE "+ConnexionBD.getNumeroContact()+" = "+contact.getNumero();

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        int id=0;

        if(c !=null ) {
            c.moveToFirst();
            id=c.getInt(c.getColumnIndex(ConnexionBD.getKeyId()));
        }
        Log.d("id","id :"+id);
        return id;
    }
    /*
     *récupérer l' ID contact de la bd à partir de son numero
     */
    public long selectionnerIDContactByName(Contact contact){
        SQLiteDatabase db=this.conn.getReadableDatabase();

        String query="SELECT id FROM "+ConnexionBD.getTableContact()+" WHERE "+ConnexionBD.getNomContact()+" = '"+contact.getNomContact()+"'";

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        long id=0;
        if(c !=null) {
            c.moveToFirst();
            id=c.getLong(c.getColumnIndex(ConnexionBD.getKeyId()));
        }

        return id;
    }
    /*
     *modifier le nom d' un contact de la bd
     */
    public int modifierNomContact(Contact contact) {
        SQLiteDatabase db=this.open();

        ContentValues values=new ContentValues();
        values.put(ConnexionBD.getNomContact(),contact.getNomContact());

        //mise à jour de l'enregistrement
        return db.update(ConnexionBD.getTableContact(),values,ConnexionBD.getKeyId()+" = ?",
                new String[]{String.valueOf(selectionnerIDContactByNumber(contact))});
    }

    /*
     *modifier le numero d'un contact de la bd
     */
    public int modifierNumeroContact(Contact contact) {
        SQLiteDatabase db=this.open();

        ContentValues values=new ContentValues();
        values.put(ConnexionBD.getNumeroContact(),contact.getNumero());

        //mise à jour de l'enregistrement
        return db.update(ConnexionBD.getTableContact(),values,ConnexionBD.getKeyId()+" = ?",
                new String[]{String.valueOf(selectionnerIDContactByName(contact))});

    }

    /*
     *supprimer un contact de la bd à partir de son identifiant
     */
    public boolean supprimerContact(Contact contact){
        SQLiteDatabase db=this.open();

        int reponse=db.delete(ConnexionBD.getTableContact(),ConnexionBD.getKeyId()+" = ?",
                new String []{String.valueOf(selectionnerIDContactByNumber(contact))});

        this.closeDB();
        return (reponse !=0);
    }


    /*
     *récupérer tous les contacts de la bd
     */
    public List<Contact> selectionnerTousLesContacts(){
        SQLiteDatabase db=this.conn.getReadableDatabase();
        List<Contact> contacts=new ArrayList<Contact>();
        String query="SELECT * FROM "+ConnexionBD.getTableContact();

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        if(c.moveToFirst()){
            do{
                Contact contact=new Contact();
                contact.setNomContact(c.getString(c.getColumnIndex(ConnexionBD.getNomContact())));
                contact.setNumero(c.getInt(c.getColumnIndex(ConnexionBD.getNumeroContact())));

                contacts.add(contact);
            }while(c.moveToNext());
        }

        c.close();
        this.closeDB();
        return contacts;
    }
    /*
     *récupérer tous les contacts d'un groupe
     */
    public ArrayList<Contact> selectionnerContactsGroupe(int groupe_id){
        SQLiteDatabase db=this.conn.getReadableDatabase();
        ArrayList<Contact> contacts=new ArrayList<Contact>();
        String query="SELECT * FROM "+ConnexionBD.getTableGroupeContact()+" WHERE "+ConnexionBD.getGcIdGroupe()+" = "+groupe_id;

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        if(c.moveToFirst()){
            do{
                Contact contact=selectionnerContact(c.getInt(c.getColumnIndex(ConnexionBD.getGcIdContact())));
                contacts.add(contact);
            }while(c.moveToNext());
        }

        c.close();
        return contacts;
    }

    public Contact_DAO(Context context) {
        super(context);
    }
}

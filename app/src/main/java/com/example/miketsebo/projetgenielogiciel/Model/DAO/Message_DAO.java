package com.example.miketsebo.projetgenielogiciel.Model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.miketsebo.projetgenielogiciel.Model.BD.ConnexionBD;
import com.example.miketsebo.projetgenielogiciel.Model.principal.Contact;
import com.example.miketsebo.projetgenielogiciel.Model.principal.Message;

import java.sql.Date;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;


/*
     A MODIFIER
 */

public class Message_DAO extends DAOBase{

        Groupe_DAO groupeDao;
    /*
     *créer un contact
     */
    public long enregisterMessage(Message message){
        SQLiteDatabase db= this.conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(ConnexionBD.getTITRE(),message.getTitre());
        values.put(ConnexionBD.getCONTENU(),message.getContenu());
        values.put(ConnexionBD.getDateEnvoi(), String.valueOf(Calendar.getInstance().getTime()));
        values.put(ConnexionBD.getDateModification(), String.valueOf(Calendar.getInstance().getTime()));

        //insertion dans la BD
        long message_res=db.insert(ConnexionBD.getTableMessage(),null,values);
        this.closeDB();

        return message_res;
    }

    /*
     *récupérer un message de la bd à partir de son identifiant
     */
    public Message selectionnerMessage(long message_id){
        SQLiteDatabase db=this.conn.getReadableDatabase();

        String query="SELECT * FROM "+ConnexionBD.getTableMessage()+" WHERE "+ConnexionBD.getKeyId()+" = "+message_id;

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        if(c !=null)
            c.moveToFirst();
        Date dateEnvoi= (Date)Calendar.getInstance().getTime();
        Date dateModif=(Date)Calendar.getInstance().getTime(); ;
        SimpleDateFormat format=new SimpleDateFormat("'yyyy-MM-dd'-'HH:mm;ss'");
        try {
            dateEnvoi = (Date)format.parse(c.getString(c.getColumnIndex(ConnexionBD.getDateEnvoi())));
            dateModif = (Date)format.parse(c.getString(c.getColumnIndex(ConnexionBD.getDateModification())));
        }catch(ParseException e){
            e.printStackTrace();
        }
        Message message=new Message(
                c.getString(c.getColumnIndex(ConnexionBD.getCONTENU())),
                dateEnvoi,dateModif,
                c.getString(c.getColumnIndex(ConnexionBD.getTITRE())));
            c.close();
        return message;

    }

    /*
     *modifier tous les champs d'un message
     */
    public int modifierToutLeMessage(Message message) {
        SQLiteDatabase db=this.conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(ConnexionBD.getCONTENU(),message.getContenu());
        values.put(ConnexionBD.getTITRE(),message.getTitre());
        values.put(ConnexionBD.getDateEnvoi(), String.valueOf(Calendar.getInstance().getTime()));
        values.put(ConnexionBD.getDateModification(), String.valueOf(Calendar.getInstance().getTime()));

        //mise à jour de l'enregistrement
        return db.update(ConnexionBD.getTableContact(),values,ConnexionBD.getKeyId()+" = ?",
                new String[]{String.valueOf(message.getId())});
    }

    /*
     *supprimer un message de la bd à partir de son identifiant
     */
    public boolean supprimerContact(String titre){
        SQLiteDatabase db=this.conn.getWritableDatabase();

        int reponse=db.delete(ConnexionBD.getTableMessage(),ConnexionBD.getKeyId()+" = ?",
                new String []{String.valueOf(selectionnerIdMessage(titre))});

        return (reponse!=0);
    }

    /*
        recupérer l'id d'un message à partir de son titre
     */
    public int selectionnerIdMessage(String titre){
        SQLiteDatabase db=this.conn.getReadableDatabase();

        String query="SELECT * FROM "+ConnexionBD.getTableMessage()+" WHERE "+ConnexionBD.getTITRE()+" = "+titre;

        Log.e(ConnexionBD.getLOG(),query);

        Cursor c=db.rawQuery(query,null);

        if(c !=null)
            c.moveToFirst();

        int id=c.getInt(c.getColumnIndex(ConnexionBD.getKeyId()));
        c.close();
        return id;
    }

    public Message_DAO(Context context) {
            super(context);
            groupeDao=new Groupe_DAO(context);
    }
}

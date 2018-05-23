package com.example.miketsebo.projetgenielogiciel;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miketsebo.projetgenielogiciel.Model.DAO.Contact_DAO;
import com.example.miketsebo.projetgenielogiciel.Model.principal.Contact;
import com.example.miketsebo.projetgenielogiciel.Utils.MyDividerItemDecoration;
import com.example.miketsebo.projetgenielogiciel.Utils.RecyclerTouchListener;
import com.example.miketsebo.projetgenielogiciel.view.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactAdapter mAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    Contact_DAO contact_dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_contact_view);

        contact_dao=new Contact_DAO(getApplicationContext());

        //création des contacts
        Contact contact1=new Contact("julie",693716045);
        Contact contact2=new Contact("cynthia",699808568);
        Contact contact3=new Contact("yori",699733578);


        //insertion dans la bd
        long contact1_id=contact_dao.enregisterContact(contact2);
        long contact2_id=contact_dao.enregisterContact(contact1);
        long contact3_id=contact_dao.enregisterContact(contact3);


        contactList.addAll(contact_dao.selectionnerTousLesContacts());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    showContactDialog(false, null, -1);
                }

        });


        mAdapter = new ContactAdapter(this, contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));


    }
    /**
     * Inserting new note in db
     * and refreshing the list
     */
    private boolean createContact(Contact Contact) {
        // inserting note in db and getting
        // newly inserted note id
        long id = contact_dao.enregisterContact(Contact);

        // get the newly inserted note from db
        Contact n = contact_dao.selectionnerContact(id);

        if (n != null) {
            // adding new note to array list at 0 position
            contactList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
        return (id !=0);
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private boolean updateNote(String nomContact,int number, int position) {
        Contact n = contactList.get(position);
        // updating note text
        n.setNomContact(nomContact);

        // updating note in db
        contact_dao.modifierNomContact(n);
        n.setNumero(number);
        int success=contact_dao.modifierNumeroContact(n);
        // refreshing the list
        contactList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
        return (success !=0);
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private boolean deleteNote(int position) {
        // deleting the note from db
        boolean success=contact_dao.supprimerContact(contactList.get(position));

        // removing the note from the list
        contactList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
        return success;
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showContactDialog(true, contactList.get(position), position);
                } else {
                    if(deleteNote(position))
                        Toast.makeText(MainActivity.this, "Contact deleted",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Contact not deleted",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }

        private void toggleEmptyNotes() {
            // you can check notesList.size() > 0

            if (contact_dao.selectionnerTousLesContacts().size() > 0) {
                noNotesView.setVisibility(View.GONE);
            } else {
                noNotesView.setVisibility(View.VISIBLE);
            }
        }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a contact.
     * when shouldUpdate=true, it automatically displays old contact and changes the
     * button text to UPDATE
     */
    private void showContactDialog(final boolean shouldUpdate, final Contact contact, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.contact_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.contact);
        final EditText inputTimsestamp =view.findViewById(R.id.timestamp);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_contact_title) : getString(R.string.lbl_edit_contact_title));

        if (shouldUpdate && contact != null) {
            inputNote.setText(contact.getNomContact());
            inputTimsestamp.setText(String.valueOf(contact.getNumero()));
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && contact != null) {
                    // update note by it's id
                    if(updateNote(inputNote.getText().toString(),Integer.parseInt(inputTimsestamp.getText().toString()), position))
                        Toast.makeText(MainActivity.this, "Contact Updated succesfully",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Contact not Updated",Toast.LENGTH_LONG).show();
                } else {
                    // create new note
                    if(createContact(new Contact(inputNote.getText().toString(),Integer.parseInt(inputTimsestamp.getText().toString()))))
                        Toast.makeText(MainActivity.this, "Contact inserted succesfully",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Contact not inserted",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

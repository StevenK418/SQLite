package com.example.sqlite;

import android.os.Bundle;
import java.util.List;
import android.app.ListActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class MainActivity extends ListActivity
{
    private CommentsDataSource datasource;
    public EditText userText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userText = (EditText) findViewById(R.id.input1);

        datasource = new CommentsDataSource(this);
        datasource.open();

        List<Comment> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        //Populate the contacts db
        PopulateContacts();
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;
        switch (view.getId()) {
            case R.id.add:
                String inStr = userText.getText().toString();
                // save the new comment to the database
                comment = datasource.createComment(inStr);
                adapter.add(comment);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    comment = (Comment) getListAdapter().getItem(0);
                    datasource.deleteComment(comment);
                    adapter.remove(comment);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume()
    {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        datasource.close();
        super.onPause();
    }

    public void PopulateContacts()
    {
        ContactsDatabaseHandler db = new ContactsDatabaseHandler(this);

        //db.emptyContacts();     // empty table if required

        // Inserting Contacts
        Log.i("Insert: ", "Inserting ..");
        db.addContact(new Contact("Joe", "0873456789"));
        db.addContact(new Contact("Mary", "0863111122"));
        db.addContact(new Contact("Jack", "0859999888"));
        db.addContact(new Contact("Andrew", "083334444"));
        db.addContact(new Contact("Harold", "0831112222"));
        db.addContact(new Contact("Joe", "0835554444"));

        // Reading all contacts
        Log.i("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " +
                    cn.getPhoneNumber();
            // Writing Contacts to log
            Log.i("Name: ", log);
        }

        Log.i("divider", "====================");

        Contact singleUser = db.getContact(5);
        Log.i("contact 5 is ", singleUser.getName());

        Log.i("divider", "====================");

        // Calling SQL statement
        int userCount = db.getContactsCount();
        Log.i("User count: ", String.valueOf(userCount));

        //Get all of the Joes
        GetAllWithNameJoe(db);
    }

    // Gets all the contacts whose name is Joe
    public void GetAllWithNameJoe(ContactsDatabaseHandler db )
    {
        //Create a new list to store the result of teh query
        List<Contact> joeContacts =  db.getAllContactsWithNameJoe();
        //Iterate through the result and print
        for (Contact cn : joeContacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " +
                    cn.getPhoneNumber();
            // Writing Contacts to log
            Log.i("Name: ", log);
        }
    }

}

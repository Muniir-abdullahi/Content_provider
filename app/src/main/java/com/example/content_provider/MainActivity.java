package com.example.content_provider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView txtContacts;
    //to store the phonebook
    ArrayList<String> arrayList;
    String[] projection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtContacts = (TextView)findViewById(R.id.contacts);
        //to initialize the memory to arraylist
        arrayList = new ArrayList<>();
        //give runtime permission in read contacts
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        && checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
        ){
            // now request the permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            //to get the phone
            getContacts();
        }
       
    }

    private void getContacts() {
        //to pass all phonebook to cursor
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
                );
        //to fetch all contacts from the cursor
        while (cursor.moveToNext()){
            //pass the data into string from cursor
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            // now the data into array list
            arrayList.add(name + "\n" + mobile);
            //to attach the array list into textView
            txtContacts.setText(arrayList.toString());
        }
    }
    //to get out of the runtime permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //now the permission is granted call function again
                getContacts();
            }
        }
    }
}
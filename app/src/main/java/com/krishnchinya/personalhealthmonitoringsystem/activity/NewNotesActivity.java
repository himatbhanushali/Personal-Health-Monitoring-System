package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.fragments.MainMenu_Notes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewNotesActivity extends AppCompatActivity {

    DB_Setter_Getter db_setter_getter;
    DB_Handler db_handler;

    String noteid;
    String[] notedetails;
    EditText title;
    EditText description;
    ImageButton cameraButton;
    private static final int REQUEST_CODE=1;
    private Bitmap bitmap;
    Bundle extras;
    private ImageView notesImage;
    ByteArrayOutputStream stream;
    byte[] imageByte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);

        title = (EditText) findViewById(R.id.etTitle);
        description = (EditText) findViewById(R.id.etDescription);
        notesImage=(ImageView)findViewById(R.id.notesImage);
        db_handler = new DB_Handler(NewNotesActivity.this);

        cameraButton=(ImageButton) findViewById(R.id.camerabutton);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

        String calling_activity = getIntent().getStringExtra("caller");
        if(!calling_activity.equals("newNote")){

            noteid = getIntent().getStringExtra("caller");

            notedetails = db_handler.getNote(db_setter_getter, noteid);
            title.setText(notedetails[0]);
            description.setText(notedetails[1]);

            imageByte = db_handler.getNoteImage(db_setter_getter, noteid);
            //bitmap = Drawable.createFromStream(new ByteArrayInputStream(imageByte),null)
            if(imageByte != null) {
                notesImage.setImageBitmap(BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
            }
        }else
        {


        }

    }

    @Override
    public void onBackPressed(){
        db_handler = new DB_Handler(NewNotesActivity.this);

        if(title.getText().toString().isEmpty())
        {
            Toast t = Toast.makeText(NewNotesActivity.this,"Please Enter the Title",Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        String calling_activity = getIntent().getStringExtra("caller");
        if(calling_activity.equals("newNote")) {

            //to get new noteid
            int count = db_handler.getNotesCount();
            count = count + 1;
            //String notecount = ""+count;

            //get title, decription and noteid
            db_setter_getter = new DB_Setter_Getter(String.valueOf(count),"krishna@gmail.com",title.getText().toString(),description.getText().toString(),"",imageByte);

            //save to db
            db_handler.addNotes(db_setter_getter);
            Intent intent = new Intent(NewNotesActivity.this, MainMenu.class);
                     //startActivityForResult(intent,1);
            startActivity(intent);
            finish();


        }
        else {
            db_setter_getter = new DB_Setter_Getter("","");
            //get updated title and description
            db_setter_getter.setNotename(title.getText().toString());
            db_setter_getter.setDescription(description.getText().toString());
            db_setter_getter.setBytesarray(imageByte);

            //update the db
            db_handler.updateNote(db_setter_getter, calling_activity);
            finish();

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        InputStream stream =null;

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            extras = data.getExtras();
        bitmap = (Bitmap)extras.get("data");
        notesImage.setImageBitmap(bitmap);
        imageByte=getBytesFromButmap(bitmap);

    }

    public byte[] getBytesFromButmap(Bitmap bitmap){
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();

    }

}

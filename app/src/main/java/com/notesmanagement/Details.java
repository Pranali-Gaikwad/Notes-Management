package com.notesmanagement;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends AppCompatActivity {
    TextView details, date1, time;
    Button d;
    NotesManagementDatabase database;
    Notes  notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        d=(Button) findViewById(R.id.delete);
        details = findViewById(R.id.dContent);
        date1 = findViewById(R.id.dDate);
        time = findViewById(R.id.dTime);






        Intent intent = getIntent();
        Long id1 = intent.getLongExtra("ID", 0);
        database=new NotesManagementDatabase(this);
        notes=database.getOneNote(id1);

        details.setText(notes.get_content());
        date1.setText(notes.get_dateOfCreation());
        time.setText(notes.get_time());
        getSupportActionBar().setTitle(notes.get_title());

     Toast.makeText(this, "title -> " +notes.get_title() , Toast.LENGTH_SHORT).show();



        d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                database.deleteNote(notes.get_id());
                Toast.makeText(getApplicationContext(),"Note Successfully Deleted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));


            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Toast.makeText(this, "Edit button pressed",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this, Edit.class);
            intent.putExtra("ID", notes.get_id());
            startActivity(intent);


        }
        return super.onOptionsItemSelected(item);
    }

}

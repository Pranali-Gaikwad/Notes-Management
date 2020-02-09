package com.notesmanagement;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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



        d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            notes=new Notes();
            database.deleteNote(notes.get_id());
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });


        Intent intent = getIntent();
        Long id1 = intent.getLongExtra("ID", 0);
        String t = intent.getStringExtra("title");
        String c = intent.getStringExtra("Details");
        String d = intent.getStringExtra("date");
        String w = intent.getStringExtra("time");
        details.setText(c);
        date1.setText(d);
        time.setText(w);

        getSupportActionBar().setTitle(t);

        Toast.makeText(this, "ID -> " + notes.get_dateOfCreation(), Toast.LENGTH_SHORT).show();

      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Toast.makeText(this, "Edit button pressed",Toast.LENGTH_SHORT).show();


        }
        return super.onOptionsItemSelected(item);
    }




}

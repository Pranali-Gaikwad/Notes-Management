package com.notesmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    EditText noteTitle;
    EditText noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        noteTitle=findViewById(R.id.note_title);
        noteDetails=findViewById(R.id.note_details);


            noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                {
                    getSupportActionBar().setTitle(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


            c=Calendar.getInstance();
            todaysDate=c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
            currentTime=pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
        Log.d("calender", " Date and Time "  + todaysDate +  " and " + currentTime);

    }

    private String pad(int i) {
        if(i<10)
        {
         return "0"+i;
        }
        return String.valueOf(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;

    }
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {

            NotesManagementDatabase notesManagementDatabase=new NotesManagementDatabase(this);

            notesManagementDatabase.addNoteInDatabase(new Notes(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime));
          Toast.makeText(this,"save b pressed", Toast.LENGTH_SHORT).show();
            gotoMain();


        }
        if (item.getItemId() == R.id.delete) {
            Toast.makeText(this,"Note not save", Toast.LENGTH_SHORT).show();
            gotoMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoMain() {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        super.onBackPressed();

    }

}

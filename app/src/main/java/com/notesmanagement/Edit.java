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

public class Edit extends AppCompatActivity {
    EditText noteTitle;
    EditText noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    NotesManagementDatabase db;
    Notes n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Long id1 = intent.getLongExtra("ID", 0);
         db=new NotesManagementDatabase(this);
          n=db.getOneNote(id1);

        noteTitle=findViewById(R.id.note_title);
        noteDetails=findViewById(R.id.note_details);
        getSupportActionBar().setTitle(n.get_title());
        noteTitle.setText(n.get_title());
        noteDetails.setText(n.get_content());



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
            if(noteTitle.getText()!= null){
                n.set_title(noteTitle.getText().toString());
                n.set_content(noteDetails.getText().toString());
                n.set_dateOfCreation(todaysDate);
                n.set_time(currentTime);
                int i=db.editNote(n);
                if(i==n.get_id())
                {

                    Toast.makeText(this,"Note Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Error Occured", Toast.LENGTH_SHORT).show();
                }

                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("ID", n.get_id());
                startActivity(intent);

            }





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


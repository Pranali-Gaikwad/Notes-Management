package com.notesmanagement;

import java.util.Calendar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class AddActivity extends AppCompatActivity {

    TextView dateshow;
    TextView timeshow;
    EditText noteTitle;
    EditText noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    Button saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }


        noteTitle=findViewById(R.id.note_title);
        noteDetails=findViewById(R.id.note_details);
        dateshow=(TextView) findViewById(R.id.dateadd);
        timeshow=(TextView)findViewById(R.id.timeadd);

         saved=(Button)findViewById(R.id.saveDataButton);

         c=Calendar.getInstance();
        todaysDate=c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
        currentTime=pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
        Log.d("calender", " Date and Time "  + todaysDate +  " and " + currentTime);
        Log.d("to" ,"date"+todaysDate);

        dateshow.setText(String.valueOf(todaysDate));
        timeshow.setText(String.valueOf(currentTime));


         saved.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if((!isEmpty(noteTitle)) || (!isEmpty(noteDetails)))
                 {
                     NotesManagementDatabase db = new NotesManagementDatabase(v.getContext());
                     db.addNoteInDatabase(new Notes(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime));
                     Toast.makeText(v.getContext(), "Note Successfully Saved", Toast.LENGTH_SHORT).show();
                     gotoMain();
                 }
                 else {
                     Toast.makeText(v.getContext(), " Empty Note can not be Saved", Toast.LENGTH_SHORT).show();
                 }

             }
         });

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
      /* if (item.getItemId() == R.id.save) {
            NotesManagementDatabase db=new NotesManagementDatabase(this);
            db.addNoteInDatabase(new Notes(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime));
            Toast.makeText(this,"Note Successfully Saved", Toast.LENGTH_SHORT).show();
            gotoMain();


        }*/
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

    boolean isEmpty(EditText text)
    {
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);

    }
}

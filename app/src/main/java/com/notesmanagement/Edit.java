package com.notesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Edit extends AppCompatActivity {
    EditText noteTitle, noteDetails;
    String todayDate;
    NotesManagementDatabase database;
    Button edit;
    Notes note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }

        Intent intent = getIntent();
        Long id1 = intent.getLongExtra("ID", 0);

        database = new NotesManagementDatabase(this);
        note = database.getOneNote(id1);

        noteTitle = findViewById(R.id.note_title);
        noteDetails = findViewById(R.id.note_details);

        edit = findViewById(R.id.saveEditButton);
        getSupportActionBar().setTitle(note.get_title());
        noteTitle.setText(note.get_title());
        noteDetails.setText(note.get_content());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!isEmpty(noteTitle)) || (!isEmpty(noteDetails))) {
                    note.set_title(noteTitle.getText().toString());
                    note.set_content(noteDetails.getText().toString());
                    note.set_dateOfCreation(todayDate);
                    long i = database.editNote(note);
                    if (i == note.get_id()) {
                    } else {
                        Toast.makeText(v.getContext(), "Note Update Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Empty Note can not be Save Successfully", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("ID", note.get_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete) {

            Toast.makeText(this, "Note not Update Successfully", Toast.LENGTH_SHORT).show();
            gotoMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();

    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);

    }
}


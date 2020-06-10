package com.notesmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Details extends AppCompatActivity {
    TextView noteDetails, dateToShow;
    ImageButton deleteButton;
    NotesManagementDatabase database;
    Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }


        deleteButton = findViewById(R.id.delete);
        noteDetails = findViewById(R.id.dContent);
        dateToShow = findViewById(R.id.dDate);

        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        long id1 = intent.getLongExtra("ID", 0);

        database = new NotesManagementDatabase(this);
        notes = database.getOneNote(id1);

        noteDetails.setText(notes.get_content());
        dateToShow.setText(notes.get_dateOfCreation());

        getSupportActionBar().setTitle(notes.get_title());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog diaBox = AskOptionToDelete();
                diaBox.show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(this, Edit.class);
            intent.putExtra("ID", notes.get_id());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOptionToDelete() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Delete this note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        database.deleteNote(notes.get_id());
                        showToastMethod("Note Successfully Deleted");
                        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return alertDialog;
    }
    public void showToastMethod(String message){
        View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.linLay));
        TextView textViewToShowMsg= toastView.findViewById(R.id.textViewMsgForToast);
        textViewToShowMsg.setText(message);
        Toast toast=new Toast(Details.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

}

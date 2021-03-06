package com.notesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class Folders extends AppCompatActivity {

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.folder_setting, menu);
    return true;

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_folders);


    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
    }


    final ListView list = findViewById(R.id.list);
    ArrayList<String> arrayList = new ArrayList<>();
    arrayList.add("All Notes");
    arrayList.add("Trash");
    arrayList.add("Study");

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
    list.setAdapter(arrayAdapter);


    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clickedItem = (String) list.getItemAtPosition(position);
        if (clickedItem.equals("All Notes")) {
          Intent intent = new Intent(Folders.this, MainActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
        }
        showToastMethod("All Notes");
      }

    });

  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.setting) {
      Intent intent = new Intent(this, Settings.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }
    return super.onOptionsItemSelected(item);
  }

  public void showToastMethod(String message){
    View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.linLay));
    TextView textViewToShowMsg= toastView.findViewById(R.id.textViewMsgForToast);
    textViewToShowMsg.setText(message);
    Toast toast=new Toast(Folders.this);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(toastView);
    toast.setGravity(Gravity.BOTTOM,0,0);
    toast.show();
  }
}
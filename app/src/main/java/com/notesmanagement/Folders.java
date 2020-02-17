package com.notesmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Folders extends AppCompatActivity {

Button b;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.folder_setting, menu);
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

       // b=(Button)findViewById(R.id.)
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }


        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("All Notes");
        arrayList.add("Trash");
        arrayList.add("Study");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                if (clickedItem.equals("All Notes"))
                {
                    Intent intent=new Intent(Folders.this, MainActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(Folders.this,clickedItem,Toast.LENGTH_LONG).show();
            }

        });
       /* b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openBox();
            }
        });
*/

    }

    private void openBox() {

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent=new Intent(this, Settings.class);
            startActivity(intent);


        }
        return super.onOptionsItemSelected(item);
    }
}

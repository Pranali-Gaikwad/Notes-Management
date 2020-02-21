package com.notesmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnLongClickListener{
    RecyclerView recyclerView;
    Adapter adapter;
    List<Notes> notes;
    Button addition;
    Notes notes1;
    private ActionMode mactionMode;
    RelativeLayout layout;
    NotesManagementDatabase database;
    List<CheckBox> c;
    Menu context_menu;
    boolean isMultiSelect = false;
    List<Notes> toDelete;

    boolean is_in_action_mode= false;
    TextView counter;
    int count=0;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resource, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }

        layout= findViewById(R.id.main);
        recyclerView = findViewById(R.id.review);
        NotesManagementDatabase db1 = new NotesManagementDatabase(this);

        notes = db1.getListOfNotes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapter(this, notes);
        toDelete=new ArrayList<>();

        recyclerView.setAdapter(adapter);

        Collections.reverse(notes);
        addition = (Button) findViewById(R.id.additionButton);

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        enableSwipe();
    }



    private void enableSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            public static final float ALPHA_FULL = 1.0f;

            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint p = new Paint();

                    if (dX > 0) {
                        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_delete_black_24dp);
                        //color : right side (swiping towards left)

                        p.setARGB(255, 150,166 ,216 );

                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    } else {

                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_delete_black_24dp);
                    p.setARGB(255, 150,166 ,216 );
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    }
                    final float alpha = ALPHA_FULL - Math.abs(dX) / width;
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }

        private int convertDpToPx(int dp) {
            return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }

        @Override
        public void onSwiped (@NonNull RecyclerView.ViewHolder viewHolder,int direction){

            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                final NotesManagementDatabase db = new NotesManagementDatabase(getApplicationContext());
                final Notes temp=notes.get(position);
                final Notes recover=notes.get(position);
              //  notes.remove(position);

                db.deleteNote(temp.get_id());
                adapter.notifyItemRemoved(position);
              //  Toast.makeText(getApplicationContext(), "swiped to left", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(layout, "Removed from list", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    public void onClick(View view) {
                     //  notes.add(position,recover);
                        db.addNoteInDatabase(recover);
                       adapter.notifyItemInserted(position);

                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            } else if (direction == ItemTouchHelper.RIGHT) {
                final NotesManagementDatabase db = new NotesManagementDatabase(getApplicationContext());
                final Notes temp=notes.get(position);
                final Notes recover=notes.get(position);
                db.deleteNote(temp.get_id());
                adapter.notifyItemRemoved(position);
                //  Toast.makeText(getApplicationContext(), "swiped to left", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(layout, "Removed from list", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    public void onClick(View view) {
                        //  notes.add(position,recover);
                        db.addNoteInDatabase(recover);
                        adapter.notifyItemInserted(position);

                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

}

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.folder) {
            Intent intent=new Intent(this, Folders.class);
            startActivity(intent);
        }
        if (item.getItemId()==R.id.deletelist)
        {

            AlertDialog diaBox = AskOption();
            diaBox.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Delete this note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        c=new ArrayList<CheckBox>();
                        for (CheckBox d: c)
                        {
                            if(d.isChecked()){
                                //notes1=new ArrayList<Notes>(notes);
                                database.deleteNote(notes1.get_id());
                            }
                        }

                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    @Override
    public boolean onLongClick(View v) {

        if (mactionMode != null) {
            return false;
        }
        mactionMode = v.startActionMode(actionModeCallbacks);
        v.setSelected(true);

        is_in_action_mode=true;
        adapter.notifyDataSetChanged();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        return true;



    }
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.delete_hidden, menu);
            mode.setTitle("0 Notes Selected");
            return  true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_hidden:
                    Log.d("delete","pressed");
                    is_in_action_mode=false;
                    for (Notes n: toDelete)
                    {
                        notes.remove(n);
                        adapter.notifyDataSetChanged();
                    }

                    // Toast.makeText("this","Pressed",Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            onBackPressed();
     }


    };

    public void prepareSelection(View view, int position){
        if (((CheckBox)view).isChecked()){
            toDelete.add(notes.get(position));
            count=count+1;
            updateCounter(count);
        }
        else {
            toDelete.remove(notes.get(position));
            count=count-1;
            updateCounter(count);
        }

    }

    public void updateCounter(int c){
        if (count==0){

           // mactionMode.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            mactionMode.setTitle("0 Notes selected");
        }
        else {
            mactionMode.setTitle(count+ " Notes selected");
        }
    }

    @Override
    public void onBackPressed() {
        count=0;

       is_in_action_mode = false;
       toDelete.clear();
       mactionMode = null;
       adapter.notifyDataSetChanged();

   // super.onBackPressed();
}


    }




package com.notesmanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    RecyclerView recyclerView;
    Adapter adapter;
    List<Notes> notes = new ArrayList<>();
   List<Notes> multiselect_list = new ArrayList<>();
    Button addition;
    Notes notes1;
    RelativeLayout layout;
    NotesManagementDatabase database;
    List<CheckBox> c;
    int selectedPos;

    private List<Long> selectedIds = new ArrayList<>();
    boolean isMultiSelect = false;
    boolean is_in_action_mode = false;
    int count = 0;
    private ActionMode mactionMode;
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {


            mode.getMenuInflater().inflate(R.menu.delete_hidden, menu);
            mode.setTitle("0 Notes Selected");
            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.delete_hidden:
                    Log.d("delete", "pressed");
                    if (multiselect_list.size() > 0) {
                        final NotesManagementDatabase db = new NotesManagementDatabase(getApplicationContext());

                        for (int i = 0; i < multiselect_list.size(); i++) {

                            db.deleteNote(multiselect_list.get(i).get_id());
                           notes.remove(multiselect_list.get(i));
                            adapter.notifyItemRemoved(i);
                        }

                        adapter.notifyDataSetChanged();
                        mode.finish();
                    }
                    return true;
                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiselect_list.clear();
            isMultiSelect = false;
            adapter.notifyDataSetChanged();
            addition.setVisibility(View.VISIBLE);
            layout.setBackgroundColor(Color.parseColor("#F7F3F3"));
            onBackPressed();
        }
    };

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

        layout = findViewById(R.id.main);
        recyclerView = findViewById(R.id.review);
        NotesManagementDatabase db1 = new NotesManagementDatabase(this);

        notes = db1.getListOfNotes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapter(this, notes, multiselect_list);


        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
               addition.setVisibility(View.GONE);
               selectedPos=position;
                    multi_select(position, view);

                } else {
                    Intent intent = new Intent(view.getContext(), Details.class);
                    intent.putExtra("ID", notes.get(position).get_id());
                    view.getContext().startActivity(intent);

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

                if (!isMultiSelect) {
                    selectedIds=new ArrayList<Long>();
                    multiselect_list = new ArrayList<Notes>();
                    isMultiSelect = true;
                    addition.setVisibility(View.GONE);
                     selectedPos=position;

                    if (mactionMode == null) {
                        mactionMode = startActionMode(actionModeCallbacks);
                    }

                }
                multi_select(position, view);

            }

        }));

        Collections.reverse(notes);
        addition = findViewById(R.id.additionButton);

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        enableSwipe();
    }

    public void multi_select(int position, View view) {


        if (mactionMode != null) {
            if (multiselect_list.contains(notes.get(position))) {
                multiselect_list.remove(notes.get(position));

                view.setBackgroundColor(Color.parseColor("#F7F3F3"));
            } else {
                if (selectedPos==position) {
                    multiselect_list.add(notes.get(position));
                    view.setBackgroundColor(Color.parseColor("#8bcfed"));
                }
                else {
                    view.setBackgroundColor(Color.parseColor("#F7F3F3"));
                }
            }
            if (multiselect_list.size() > 0) {
                mactionMode.setTitle(multiselect_list.size() + " Notes Selected");

            } else {
                mactionMode.setTitle("Select Notes");

            }
        }


    }
    private void enableSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
          public static final float ALPHA_FULL = 1.0f;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint p = new Paint();

                    if (dX > 0) {

                        p.setARGB(255, 150, 166, 216);

                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    } else {

                        p.setARGB(255, 150, 166, 216);
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

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT ) {
                    final NotesManagementDatabase db = new NotesManagementDatabase(getApplicationContext());
                    final Notes temp = notes.get(position);
                    final Notes recover = notes.get(position);
                    db.deleteNote(temp.get_id());
                    adapter.notifyItemRemoved(position);

                    Snackbar snackbar = Snackbar.make(layout, "Removed from list", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        public void onClick(View view) {
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.folder) {
            Intent intent = new Intent(this, Folders.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Delete this note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        c = new ArrayList<CheckBox>();
                        for (CheckBox d : c) {
                            if (d.isChecked()) {
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
    public void onBackPressed() {
        count = 0;

        is_in_action_mode = false;
        multiselect_list.clear();
        mactionMode = null;
        adapter.notifyDataSetChanged();

    }

    private void gotomain() {
        super.onBackPressed();

    }
}




package com.notesmanagement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
     private LayoutInflater inflater;
    private List<Notes> notes;

    Adapter(Context context, List<Notes> notes)
    {
        this.inflater=LayoutInflater.from(context);
        this.notes=notes;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        holder.nTitle1.setText(notes.get(position).get_title());
        String w= notes.get(position).get_dateOfCreation();
        holder.nDate1.setText(w);
        String p= notes.get(position).get_time();
        holder.nTime1.setText(p);
        Log.d("date", "date time" +w);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nTitle1, nDate1, nTime1;


        public ViewHolder(View itemView)
        {
            super(itemView);
            nTitle1 =(TextView) itemView.findViewById(R.id.nTitle);
            nDate1 = (TextView) itemView.findViewById(R.id.nDate);
            nTime1 =(TextView)  itemView.findViewById(R.id.nTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(),"Item Pressed",Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
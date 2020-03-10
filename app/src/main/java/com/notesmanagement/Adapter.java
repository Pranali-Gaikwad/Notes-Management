package com.notesmanagement;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private List<Notes> notelistAll;
    private List<Notes> selectedItems = new ArrayList<>();
    private List<Notes> notes;

    Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Notes> filteredlist = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredlist.addAll(notelistAll);
                Log.d("list", "all list " + notelistAll);

            } else {

                for (Notes n1 : notelistAll) {
                    if (n1.get_title().toLowerCase().contains(constraint.toString().toLowerCase())
                            || n1.get_content().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredlist.add(n1);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            notes.clear();
            notes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public Adapter(Context context, List<Notes> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.notelistAll = new ArrayList<>(notes);

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.ViewHolder holder, int position) {

        holder.nTitle1.setText(notes.get(position).get_title());
        holder.nDate1.setText(notes.get(position).get_dateOfCreation());
        Notes n = notes.get(position);

        if (selectedItems.contains(n)){

            holder.cardView.setCardBackgroundColor(Color.parseColor("#CFF6FC"));

        }
        else {

            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }


    }


    @Override
    public int getItemCount() {
        return notes.size();
    }
    public void setSelectedIds(List<Notes> selectedItems) {
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    public void setListOfAllNotes(List<Notes> updateList){
        this.notelistAll = new ArrayList<>(updateList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nTitle1, nDate1;
        LinearLayout layout;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            nTitle1 = itemView.findViewById(R.id.nTitle);
            nDate1 = itemView.findViewById(R.id.nDate);
            layout = itemView.findViewById(R.id.linearLayout);
            cardView = itemView.findViewById(R.id.cardView);


        }

    }
}


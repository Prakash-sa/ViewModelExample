package com.example.viewmodelex;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.NoteHolder>{

    private List<Note>notes=new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);

        return new NoteHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote=notes.get(position);
        holder.textViewtitle.setText(currentNote.getTitle());
        holder.textViewDesciption.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {

        return notes.size();
    }

    public void setNotes(List<Note> notes){
        this.notes=notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position){
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView textViewtitle;
        private TextView textViewDesciption;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewDesciption=itemView.findViewById(R.id.text_description);
            textViewPriority=itemView.findViewById(R.id.text_priority);
            textViewtitle=itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(listener!=null&&pos!=RecyclerView.NO_POSITION)
                    listener.onItemClick(notes.get(pos));
                }
            });
        }
    }
    public interface OnItemClickListener
    {
        void onItemClick(Note note);
    }

    public void setOnItemClickListner(OnItemClickListener listner){
        this.listener=listner;
    }

}

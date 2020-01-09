package com.example.viewmodelex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE_REQUEST=1;
    private static final int EDIT_NOTE_REQUEST=2;

    Button bt;
    TextView textview;
    IncreseViewModel increaseViewmodel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddNoteAc.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
            }
        });


        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);

        increaseViewmodel= ViewModelProviders.of(this).get(IncreseViewModel.class);
        increaseViewmodel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                increaseViewmodel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListner(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(MainActivity.this,AddNoteAc.class);
                intent.putExtra("ID",note.getId());
                intent.putExtra("Title",note.getTitle());
                intent.putExtra("Description",note.getDescription());
                intent.putExtra("Priority",note.getPriority());
                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteallnotes:
                increaseViewmodel.deleteallnotes();
                return true;
                default:
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(resultCode==RESULT_OK){
           if(requestCode==ADD_NOTE_REQUEST){
               String title=data.getStringExtra("Title");
               String descrition=data.getStringExtra("Description");
               int priority=data.getIntExtra("Priority",1);
               Note note=new Note(title,descrition,priority);
               increaseViewmodel.insert(note);
               Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();

           }
           if(requestCode==EDIT_NOTE_REQUEST){
                int id=getIntent().getIntExtra("ID",-1);
                if(id==-1){
                    Toast.makeText(this,"Can't be Updated",Toast.LENGTH_LONG).show();
                    return;
                }
               String title=data.getStringExtra("Title");
               String descrition=data.getStringExtra("Description");
               int priority=data.getIntExtra("Priority",1);
               Note note=new Note(title,descrition,priority);
               note.setId(id);
               increaseViewmodel.update(note);
               Toast.makeText(this,"Updated",Toast.LENGTH_LONG).show();


           }
       }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

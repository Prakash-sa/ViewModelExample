package com.example.viewmodelex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNoteAc extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescrition;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextDescrition=findViewById(R.id.edit_description);
        editTextTitle=findViewById(R.id.edit_title);
        numberPickerPriority=findViewById(R.id.number_picker);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel_black_24dp);

        Intent intent=getIntent();
        if(intent.hasExtra("ID")){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra("Title"));
            editTextDescrition.setText(intent.getStringExtra("Description"));
            numberPickerPriority.setValue(intent.getIntExtra("Priority",1));
        }
        else
            setTitle("Add Note");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.note_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote(){
        String title =editTextTitle.getText().toString();
        String descritions=editTextDescrition.getText().toString();
        int priority=numberPickerPriority.getValue();
        if(title.trim().isEmpty()||descritions.trim().isEmpty()){
            Toast.makeText(this,"Please insret a",Toast.LENGTH_LONG).show();
            return;
        }



        Intent intent=new Intent();
        intent.putExtra("Title",title);
        intent.putExtra("Description",descritions);
        intent.putExtra("Priority",priority);

        int id=getIntent().getIntExtra("ID",-1);
        if(id!=-1){
            intent.putExtra("ID",id);
        }
        setResult(RESULT_OK,intent);
        finish();

    }
}

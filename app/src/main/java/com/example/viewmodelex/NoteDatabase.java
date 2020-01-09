package com.example.viewmodelex;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){

        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return  instance;
    }

    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i("             NoteDatabase","Call");
            new PopulateDbAsyncTassk(instance).execute();
        }
    };

    private static class PopulateDbAsyncTassk extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        private PopulateDbAsyncTassk(NoteDatabase db){
            noteDao=db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1","jndsjfn",1));
            noteDao.insert(new Note("Title 2","asdfdf",2));
            noteDao.insert(new Note("Title 3","adsfg",3));
            Log.i("Inserted ",noteDao.toString());

            return null;
        }
    }

}


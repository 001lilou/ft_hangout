package com.example.ft_hangout.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.example.ft_hangout.dao.ContactsDao;
import com.example.ft_hangout.entity.Contacts;


@Database(entities = {Contacts.class}, version = 2, exportSchema = false)
public abstract class ContactsDatabase extends RoomDatabase {

    private static ContactsDatabase instance;

    public abstract ContactsDao contactsDao();

    public static synchronized ContactsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ContactsDatabase.class, "contacts_database.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();

        }

    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactsDao contactsDao;

        private PopulateDBAsyncTask(ContactsDatabase db) {
            contactsDao = db.contactsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactsDao.insert(new Contacts(false, "", "Chretien", "Helene", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Petitcueno", "Sylvain", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Selvais", "Frederic", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Gaillard", "Isabelle", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Gaillard", "Robin", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Martin", "Rémi", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Smith", "laurent", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Golden", "Marie", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Amaral", "Louis", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Durant", "Sylviane", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Chatron", "Isabelle", "", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Nery", "Fabrice", "", "", "", ""));
            return null;
        }
    }
}



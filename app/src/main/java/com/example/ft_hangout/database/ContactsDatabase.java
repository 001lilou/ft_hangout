package com.example.ft_hangout.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.example.ft_hangout.dao.ContactsDao;
import com.example.ft_hangout.entity.Contacts;


@Database(entities = {Contacts.class}, version = 3, exportSchema = false)
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

    public static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactsDao contactsDao;

        public PopulateDBAsyncTask(ContactsDatabase db) {
            contactsDao = db.contactsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactsDao.insert(new Contacts(false, "", "Chretien", "Helene", "0661880384", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Petitcueno", "Sylvain", "0652414888", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Selvais", "Frederic", "0769213642", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Gaillard", "Isabelle", "0688281887", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Gaillard", "Robin", "0647002803", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Martin", "Rémi", "0777777777", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Smith", "laurent", "0788888888", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Golden", "Marie", "0761850504", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Amaral", "Luis", "0630106614", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Durant", "Sylviane", "0783486399", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Chatron", "Isabelle", "0676972070", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Nery", "Fabrice", "0799999999", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Smith", "James", "06111111111", "", "", ""));
            contactsDao.insert(new Contacts(false, "", "Winston", "Roger", "072222222222", "", "", ""));
            return null;
        }
    }
}



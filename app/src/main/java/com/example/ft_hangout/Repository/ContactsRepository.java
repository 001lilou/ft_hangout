package com.example.ft_hangout.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;


import com.example.ft_hangout.Dao.ContactsDao;
import com.example.ft_hangout.DataBase.ContactsDatabase;
import com.example.ft_hangout.Entity.Contacts;

import java.util.List;

public class ContactsRepository {
    private ContactsDao contactsDao;
    private  LiveData<List<Contacts>> allContacts;
    private Contacts _contact;
    private int _id;

    public ContactsRepository(Application application) {
        ContactsDatabase database = ContactsDatabase.getInstance(application);
        contactsDao = database.contactsDao();
        allContacts = contactsDao.getAllContacts();

    }

    public void insert(Contacts contact) {
        new InsertContactsAsyncTask(contactsDao).execute(contact);

    }

    public void update(Contacts contact) {
        new UpdateContactsAsyncTask(contactsDao).execute(contact);

    }

    public void delete(Contacts contact) {
        new DeleteContactsAsyncTask(contactsDao).execute(contact);

    }

    public void deleteAllContacts() {
        new DeleteAllContactsAsyncTask(contactsDao).execute();

    }

    public LiveData<List<Contacts>> getAllContacts() {
        return allContacts;
    }

    public Contacts selectedContact(Contacts contact) {
        _id = contact.get_id();
        _contact = contactsDao.selectedContact(_id);
        return _contact;
    }

    private static class InsertContactsAsyncTask extends AsyncTask<Contacts, Void, Void> {
        private ContactsDao contactsDao;

        private InsertContactsAsyncTask(ContactsDao contactsDao) {
            this.contactsDao = contactsDao;
        }


        @Override
        protected Void doInBackground(Contacts... contacts) {
            contactsDao.insert(contacts[0]);
            return null;
        }
    }

    private static class UpdateContactsAsyncTask extends AsyncTask<Contacts, Void, Void> {
        private ContactsDao contactsDao;

        private UpdateContactsAsyncTask(ContactsDao contactsDao) {
            this.contactsDao = contactsDao;
        }


        @Override
        protected Void doInBackground(Contacts... contacts) {
            contactsDao.update(contacts[0]);
            return null;
        }
    }

    private static class DeleteContactsAsyncTask extends AsyncTask<Contacts, Void, Void> {
        private ContactsDao contactsDao;

        private DeleteContactsAsyncTask(ContactsDao contactsDao) {
            this.contactsDao = contactsDao;
        }

        @Override
        protected Void doInBackground(Contacts... contacts) {
            contactsDao.delete(contacts[0]);
            return null;
        }
    }

    private static class DeleteAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactsDao contactsDao;

        private DeleteAllContactsAsyncTask(ContactsDao contactsDao) {
            this.contactsDao = contactsDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            contactsDao.deleteAllContacts();
            return null;
        }
    }

   /* private static class selectedContactAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ContactsDao contactsDao;

        private selectedContactAsyncTask(ContactsDao contactsDao) {
            this.contactsDao = contactsDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            contactsDao.selectedContact(integers[0]);
            return null;
        }
    }*/
}

package com.example.ft_hangout.viewmodel;

import android.app.Application;

import com.example.ft_hangout.entity.Contacts;
import com.example.ft_hangout.repository.ContactsRepository;

import java.util.List;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public class ContactsViewModel extends AndroidViewModel {
    private ContactsRepository repository;
    private LiveData<List<Contacts>> allContacts;
    private MutableLiveData<Contacts> selectedCont;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactsRepository((application));
        allContacts = repository.getAllContacts();
        selectedCont = new MutableLiveData<>();

    }

    public void insert(Contacts contact) {
        repository.insert(contact);
    }

    public void update(Contacts contact) {

        repository.update(contact);
    }

    public void delete(Contacts contact) {
        repository.delete(contact);
    }

    public void deleteAllContacts() {
        repository.deleteAllContacts();
    }

    public MutableLiveData<Contacts> getSelectedContact() {
        return selectedCont;
    }

    public LiveData<List<Contacts>> getAllContacts() {
        return allContacts;
    }
}

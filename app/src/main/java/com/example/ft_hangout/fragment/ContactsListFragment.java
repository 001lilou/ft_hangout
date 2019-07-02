package com.example.ft_hangout.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.ft_hangout.MainActivity;
import com.example.ft_hangout.entity.Contacts;
import com.example.ft_hangout.adapter.ContactsAdapter;
import com.example.ft_hangout.adapter.CustomLayoutManager;
import com.example.ft_hangout.R;
import com.example.ft_hangout.util.ThemeUtil;
import com.example.ft_hangout.viewmodel.ContactsViewModel;


import java.util.List;


public class ContactsListFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private RecyclerView recyclerView;
    private FragmentTransaction fragmentTransaction;
    private ContactsAdapter adapter;
    private List<Contacts> _contacts;
    private FloatingActionButton addContactButton;
    private Contacts currentSelectedContact;


    public ContactsListFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        addContactButton = view.findViewById(R.id.button_add_contact);
        addContactButton.setOnClickListener(v -> {
            contactsViewModel.getSelectedContact().setValue(null);
            fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactAddModifyFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        if (adapter != null && _contacts != null) {
            adapter.setContacts(_contacts);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new CustomLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new ContactsAdapter(this.getActivity(), _contacts, contact -> {
            contactsViewModel.getSelectedContact().setValue(contact);
            boolean expanded = contact.isExpanded();
            contact.setExpanded(!expanded);
            currentSelectedContact = contact;
        }, ((MainActivity)getActivity()).isPhoneActivated(), ((MainActivity)getActivity()).isSMSActivated());
        recyclerView.setAdapter(adapter);

        contactsViewModel = ViewModelProviders.of(getActivity()).get(ContactsViewModel.class);
        contactsViewModel.getAllContacts().observe(this, contacts -> {
            _contacts = contacts;
            adapter.setContacts(contacts);
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_theme:
                return true;

            case R.id.theme_1:
                ThemeUtil.changeToTheme(getActivity(), 0);
                return true;

            case R.id.theme_2:
                ThemeUtil.changeToTheme(getActivity(), 1);
                return true;
            case R.id.theme_3:
                ThemeUtil.changeToTheme(getActivity(), 3);
                return true;

            case R.id.action_language:
                Intent changerLangue = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(changerLangue);
                return true;

            case R.id.action_delete_All:
                contactsViewModel.deleteAllContacts();
                return true;

            case R.id.action_exit:
                getActivity().finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * Getters and setters
     */
    public ContactsAdapter getAdapter() {
        return adapter;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();

        // We updated state of the last selected contact before leaving fragment
        if(currentSelectedContact != null)
            currentSelectedContact.setExpanded(false);
    }
}

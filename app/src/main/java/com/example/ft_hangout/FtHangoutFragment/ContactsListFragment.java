package com.example.ft_hangout.FtHangoutFragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageButton;


import com.example.ft_hangout.Entity.Contacts;
import com.example.ft_hangout.FtHangoutAdapter.ContactsAdapter;
import com.example.ft_hangout.FtHangoutAdapter.CustomLayoutManager;
import com.example.ft_hangout.R;;
import com.example.ft_hangout.Utils.ThemeUtil;
import com.example.ft_hangout.ViewModel.ContactsViewModel;
import com.example.ft_hangout.interfaces.OnContactListener;


import java.util.List;



public class ContactsListFragment extends Fragment{

    private ContactsViewModel contactsViewModel;
    private RecyclerView recyclerView;
    private FragmentTransaction fragmentTransaction;
    private ContactsAdapter adapter;
    private Contacts _contact;
    private List<Contacts> _contacts;
    private ImageButton imgButton;
    private FloatingActionButton addContactButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int check;
    private int newcheck;


    public ContactsListFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        addContactButton = view.findViewById(R.id.button_add_contact);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactsViewModel.getSelectedContact().setValue(null);
                fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactAddModifyFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        if (adapter != null && _contacts != null)
        {
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
        adapter = new ContactsAdapter(this.getContext(), _contacts, new OnContactListener() {
            @Override
            public void onContactSelected(Contacts contact) {
                contactsViewModel.getSelectedContact().setValue(contact);
                boolean expanded = contact.isExpanded();
                contact.setExpanded(!expanded);

            }
        });
        recyclerView.setAdapter(adapter);

        contactsViewModel = ViewModelProviders.of(getActivity()).get(ContactsViewModel.class);
        contactsViewModel.getAllContacts().observe(this, new Observer<List<Contacts>>() {
            @Override
            public void onChanged(@Nullable List<Contacts> contacts) {
                _contacts = contacts;
                adapter.setContacts(contacts);
            }
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


    private void setTheme(int textAppearance_appCompat) {
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
   /*     Activity activity = (Activity) context;
        {

        }
        if (context instanceof OnItemClickListener)
            try {
                OnItemClickListener onItemClickListener = (OnItemClickListener) activity;
            } catch (ClassCastException e) {
                throw new RuntimeException((context.toString() + " must implement list.."));
            }*/
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }
}

package com.example.ft_hangout.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.arch.lifecycle.ViewModelProviders;

import com.example.ft_hangout.util.CircularImageView;
import com.example.ft_hangout.entity.Contacts;
import com.example.ft_hangout.R;
import com.example.ft_hangout.viewmodel.ContactsViewModel;

import java.util.Locale;

import static com.example.ft_hangout.util.Base64Contact.decodeBase64;


public class ContactDetailsFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private FragmentTransaction fragmentTransaction;
    private static Contacts contact;
    private CircularImageView _avatar;
    private TextView _lastname;
    private TextView _firstname;
    private TextView _numobile;
    private TextView _nufix;
    private TextView _mail;
    private TextView _address;
    private TextView _fullname;
    private String avatar;


    public ContactDetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contactsViewModel = ViewModelProviders.of(getActivity()).get(ContactsViewModel.class);
        View view = inflater.inflate(R.layout.contact_details_fragment, container, false);
        _avatar = view.findViewById(R.id.user_profile_photo);
        _fullname = view.findViewById(R.id.user_profile_name);
        _lastname = view.findViewById(R.id.lastname_Detail);
        _firstname = view.findViewById(R.id.firstname_Detail);
        _numobile = view.findViewById(R.id.mobile_Detail);
        _nufix = view.findViewById(R.id.fixe_Detail);
        _mail = view.findViewById(R.id.mail_Detail);
        _address = view.findViewById(R.id.postal_Detail);

        return view;
    }


    public void displayDetails(Contacts contact) {
        int imageResource;
        //avatar = contact.getAvatar();
        if (contact.checkAvatar() == false) {
            avatar = "@drawable/logo_42";
            imageResource = getResources().getIdentifier(avatar, null, "com.example.ft_hangout");
            Drawable res = ContextCompat.getDrawable(getContext(), imageResource);
            _avatar.setImageDrawable(res);
        } else {
            _avatar.setImageBitmap(decodeBase64(contact.getAvatar()));
        }
        _fullname.setText(contact.getLastname() + " " + contact.getFirstname());
        _lastname.setText(contact.getLastname());
        _firstname.setText(contact.getFirstname());
        _numobile.setText(String.valueOf(contact.getNumobile()));
        _nufix.setText(String.valueOf(contact.getNufix()));
        _mail.setText(contact.getMail());
        _address.setText(contact.getAddress());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getLifecycle();
        contactsViewModel = ViewModelProviders.of(getActivity()).get(ContactsViewModel.class);
        if (contactsViewModel != null) {
            contact = contactsViewModel.getSelectedContact().getValue();
            displayDetails(contact);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactAddModifyFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;

            case R.id.action_delete:
                contactsViewModel.delete(contact);
                if (Locale.getDefault().getLanguage().contentEquals("fr")) {
                    Toast.makeText(this.getContext(), "Contact supprimé !!!!", Toast.LENGTH_LONG).show();
                }
                else if (Locale.getDefault().getLanguage().contentEquals("en")) {
                    Toast.makeText(this.getContext(), "Contact deleted!", Toast.LENGTH_LONG).show();
                }

                getActivity().onBackPressed();
                return true;

            case R.id.action_exit_details:
                getActivity().finish();
                return true;

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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




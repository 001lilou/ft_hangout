package com.example.ft_hangout.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.example.ft_hangout.utils.CircularImageView;
import com.example.ft_hangout.entity.Contacts;
import com.example.ft_hangout.R;
import com.example.ft_hangout.viewmodel.ContactsViewModel;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import static com.example.ft_hangout.utils.Base64Contact.decodeBase64;
import static com.example.ft_hangout.utils.Base64Contact.encodeToBase64;


public class ContactAddModifyFragment extends Fragment implements View.OnClickListener {


    private ContactsViewModel contactsViewModel;
    private Contacts contact;
    private ImageView _avatar;
    private EditText _lastname;
    private EditText _firstname;
    private EditText _numobile;
    private EditText _nufix;
    private EditText _mail;
    private EditText _address;
    private TextView _fullname;
    private final int PICK_IMAGE_REQUEST_CODE = 1000;
    private final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001;
    private Bitmap _bitmap;
    private Uri _uriPath;
    private String _imagepath = null;
    private Boolean _updateContact = false;
    private String avatar;
    private Boolean _checkAvatar = false;


    public ContactAddModifyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_add_modify_fragment, container, false);

        _avatar = view.findViewById(R.id.image_Button_add);
        _fullname = view.findViewById(R.id.create_contact);
        _lastname = view.findViewById(R.id.lastname_add);
        _firstname = view.findViewById(R.id.firstmame_add);
        _numobile = view.findViewById(R.id.mobile_add);
        _nufix = view.findViewById(R.id.fixe_add);
        _mail = view.findViewById(R.id.mail_add);
        _address = view.findViewById(R.id.postal_add);

        ImageButton imgButtonSave = view.findViewById(R.id.save);
        ImageButton imageButtonCancel = view.findViewById(R.id.cancel);
        CircularImageView imageButtonAdd = view.findViewById(R.id.image_Button_add);

        imgButtonSave.setOnClickListener(this);
        imageButtonCancel.setOnClickListener(this);
        imageButtonAdd.setOnClickListener(this);

        return view;
    }

    public void displayDetails(Contacts contact) {

        if (contact.checkAvatar() == true) {
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

    public void saveContact() {

        String lastname = _lastname.getText().toString();
        String firstname = _firstname.getText().toString();
        String numobile = _numobile.getText().toString();
        String nufix = _nufix.getText().toString();
        String mail = _mail.getText().toString();
        String address = _address.getText().toString();
        if (_updateContact == true) {
            if (_checkAvatar == true) {
                contact.setAvatar(avatar);
                contact.setCheckAvatar(true);
            } else {
                contact.setCheckAvatar(false);
            }
            contact.setLastname(lastname);
            contact.setFirstname(firstname);
            contact.setNumobile(numobile);
            contact.setNufix(nufix);
            contact.setMail(mail);
            contact.setAddress(address);
        } else {
            contact = new Contacts(_checkAvatar, avatar, lastname, firstname, numobile, nufix, mail, address);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getLifecycle();
        contactsViewModel = ViewModelProviders.of(getActivity()).get(ContactsViewModel.class);
        if ((contact = contactsViewModel.getSelectedContact().getValue()) != null) {
            _updateContact = true;
            displayDetails(contact);
        }

    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save:
                saveContact();
                if (_lastname.getText().toString().matches("") || _numobile.getText().toString().matches("")) {
                    Toast.makeText(this.getContext(), "Can't save please update lastname and mobile number !", Toast.LENGTH_LONG).show();
                } else {
                    if (_updateContact == true) {
                        contactsViewModel.update(contact);
                        Toast.makeText(this.getContext(), "Contact updated !", Toast.LENGTH_LONG).show();
                    } else {
                        contactsViewModel.insert(contact);
                        Toast.makeText(this.getContext(), "New contact saved !", Toast.LENGTH_LONG).show();
                    }
                    getActivity().onBackPressed();

                }
                break;

            case R.id.cancel:
                getActivity().onBackPressed();
                break;

            case R.id.image_Button_add:
                pickImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bundle extras = data.getExtras();
            _uriPath = data.getData();
            try{
                _bitmap = (Bitmap) extras.get("data");
            }catch(Exception e){
                _bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), _uriPath);
            }

            if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                if (_bitmap == null) {
                    Toast.makeText(this.getContext(), "There no data to save !", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(this.getContext(), "DATA can be saved !!!!!!!!!! " + _imagepath, Toast.LENGTH_LONG).show();
                    _avatar.setImageBitmap(_bitmap);
                }
            }
            avatar = encodeToBase64(_bitmap, Bitmap.CompressFormat.JPEG, 100);
            _checkAvatar = true;

        } catch (Exception e) {
            Log.e("pickup image", e.getLocalizedMessage());
        }
    }

    private void pickImage() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openPickImage();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    public void openPickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(_uriPath, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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


package com.example.ft_hangout.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ft_hangout.MainActivity;
import com.example.ft_hangout.entity.Contacts;
import com.example.ft_hangout.R;
import com.example.ft_hangout.interfaces.OnContactListener;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import static com.example.ft_hangout.util.Base64Contact.decodeBase64;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {

    private List<Contacts> _contact;
    private Context _context;
    private OnContactListener listener;
    private int lastIndexContactSelected = -1;
    private int indexContactSelected = -1;
    private boolean isPhoneActivated, isSMSActivated;
  //  private int selectedTheme = 0;

    public ContactsAdapter(Context context, List<Contacts> contactsList, OnContactListener listener, boolean isPhoneActivated, boolean isSMSActivated) {
        this._context = context;
        this._contact = contactsList;
        this.listener = listener;
        this.isPhoneActivated = isPhoneActivated;
        this.isSMSActivated = isSMSActivated;
        if (this._contact == null)
            this._contact = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new ContactsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsHolder holder, final int position) {
        int imageResource;
        Contacts currentContact = _contact.get(position);

        if (currentContact.checkAvatar() == true) {
            holder.imageView.setImageBitmap(decodeBase64(currentContact.getAvatar()));
        } else {
            String avatar = "@drawable/logo_42";
            imageResource = this._context.getResources().getIdentifier(avatar, null, "com.example.ft_hangout");
            Drawable res = ContextCompat.getDrawable(this._context, imageResource);
            holder.imageView.setImageDrawable(res);
        }
        holder.textFulltName.setText(currentContact.getLastname() + " " + currentContact.getFirstname());
        boolean expanded = currentContact.isExpanded();
        holder.item_hide.setVisibility(expanded ? View.VISIBLE : View.GONE);

        // Managing states of button
       // selectedTheme = ThemeUtil.getTheme();
        if (((MainActivity) _context).isPhoneActivated()) {
            holder.callButton.getBackground().applyTheme(_context.getTheme());
        } else {
            holder.callButton.getBackground().setTint(_context.getResources().getColor(R.color.colorGrey));
        }
        if (((MainActivity) _context).isSMSActivated()) {
            holder.smsButton.getBackground().applyTheme(_context.getTheme());
        } else {
            holder.smsButton.getBackground().setTint(_context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return _contact.size();
    }

    public Contacts getContactAt(int position) {
        return _contact.get(position);

    }

    public void setContacts(List<Contacts> contacts) {
        this._contact = contacts;
        notifyDataSetChanged();
    }


    public class ContactsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout item_hide, item_visible;
        private ImageView imageView;
        private TextView textFulltName;
        private ImageButton callButton, smsButton;

        private ContactsHolder(View itemView) {
            super(itemView);

            item_hide = itemView.findViewById(R.id.linearlayout2);
            item_visible = itemView.findViewById(R.id.linearlayout1);
            imageView = itemView.findViewById(R.id.avatar);
            textFulltName = itemView.findViewById(R.id.textViewFullname);
            callButton = itemView.findViewById(R.id.phone);
            smsButton = itemView.findViewById(R.id.sms);

            item_visible.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            indexContactSelected = getAdapterPosition();
            if (lastIndexContactSelected == indexContactSelected) {

                //set visible item_hide
                if (_contact.get(lastIndexContactSelected).isExpanded() == false) {
                    _contact.get(lastIndexContactSelected).setExpanded(false);
                    _contact.get(indexContactSelected).setExpanded(false);
                    notifyItemChanged(getAdapterPosition());
                }
            } else if (lastIndexContactSelected != -1) {
                _contact.get(lastIndexContactSelected).setExpanded(false);
            }
            listener.onContactSelected(_contact.get(getAdapterPosition()));
            notifyItemChanged(lastIndexContactSelected);
            lastIndexContactSelected = getAdapterPosition();
            indexContactSelected = -1;
            notifyItemChanged(getAdapterPosition());
        }
    }
}







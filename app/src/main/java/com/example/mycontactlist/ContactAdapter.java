package com.example.mycontactlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    //private ArrayList<String> contactData; is replaced with <Contact>
    private final ArrayList<Contact> contactData;
//Sets a code behavior of the viewHolder
    private View.OnClickListener mOnItemClickerListener;
    private boolean isDeleting;
    private final Context parentContext;

    public static void noitfyDataSetChanged() {
    }
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewContact;
        public TextView textPhone;
        public Button deleteButton;
        public TextView textEmail;
        //delcare the constructor method for viewHolder
        public ContactViewHolder(@NonNull View itemView) {//cannot return a null value
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            textEmail = itemView.findViewById(R.id.textEmailView);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickerListener);
        }

        //Decalres a method to return the TextView layout
        public TextView getTextViewContact() {
            return textViewContact;
        }
        public TextView getPhoneTextView() {
            return textPhone;
        }
        public TextView getTextEmail() {
            return textEmail;
        }
        public Button getDeleteButton() {
            return deleteButton;
        }
    }
    //constructor method to decalre the adapter and associate data to be displayed
    public ContactAdapter(ArrayList<Contact> arrayList, Context context) {
        contactData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickerListener(View.OnClickListener itemClickerListener) {
        mOnItemClickerListener = itemClickerListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,
               parent, false);
       return new ContactViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getTextViewContact().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
        cvh.getTextEmail().setText(contactData.get(position).geteMail() );

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }
    public void setDeleting(boolean b) {
        isDeleting = b;
    }
    private void deleteItem(int position) {
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();
            if (didDelete) {
                contactData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }
}

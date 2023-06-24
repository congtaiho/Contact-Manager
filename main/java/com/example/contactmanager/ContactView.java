package com.example.contactmanager;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.contactmanager.entitie.Contact;
import com.example.contactmanager.manager.ContactManager;

import java.util.ArrayList;
import java.util.List;

public class ContactView extends LinearLayout {
    Contact contact;
    ImageView ivImage, ivImageUpdate;

    Spinner snUpdateType;

    String selectedContactType;

    EditText edNomUpdate, edPhoneUpdate;
    TextView tvNom, tvPhone, tvType;
    Button btnEdit, btnDelete, btnUpdate;

    public ContactView(Context context, Contact contact) {
        super(context);
        this.contact = contact;
        creatLayout();
    }

    private void creatLayout() {
        LinearLayout layoutContact = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_row_contact_layout, this);
        ivImage = layoutContact.findViewById(R.id.iv_image_list_row_contact);
        tvNom = layoutContact.findViewById(R.id.tv_nom_list_row_contact);
        tvPhone = layoutContact.findViewById(R.id.tv_phone_numbre_list_row_contact);
        tvType = layoutContact.findViewById(R.id.tv_type_list_row_contact);
        btnEdit = layoutContact.findViewById(R.id.btn_edit_list_row_contact);
        btnDelete = layoutContact.findViewById(R.id.btn_delete_list_row_contact);
        tvNom.setText(contact.getName());
        tvPhone.setText(contact.getPhoneNumber1());
        tvType.setText(contact.getTypeContact());

        btnEdit.setOnClickListener(v -> {
            handleClickEdit();

        });

        btnDelete.setOnClickListener(v -> {
            handleClickDeleteContact();
        });

        ivImage.setOnClickListener(v -> {
            handleChangeImage();
        });
    }

    private void handleClickEdit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog =builder.create();
        dialog.setTitle("Update contact");
        LinearLayout layoutUpdateContact = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.ad_update_contact_layout, null);

        ivImageUpdate=layoutUpdateContact.findViewById(R.id.iv_image_contact_update);
        edNomUpdate=layoutUpdateContact.findViewById(R.id.ed_nom_update);
        edNomUpdate.setText(contact.getName());
        edPhoneUpdate=layoutUpdateContact.findViewById(R.id.ed_phone_update);
        edPhoneUpdate.setText(contact.getPhoneNumber1());
        snUpdateType=layoutUpdateContact.findViewById(R.id.sn_update_type_contact);
        btnUpdate=layoutUpdateContact.findViewById(R.id.btn_update_contact);

        List<String> contactTypes = new ArrayList<>();
        contactTypes.add("Amis");
        contactTypes.add("Familles");
        contactTypes.add("Collègues");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, contactTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snUpdateType.setAdapter(adapter);
        snUpdateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedContactType = contactTypes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setView(layoutUpdateContact);
        dialog.show();

        btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //image pas encore
                String newNom = edNomUpdate.getText().toString().trim();
                String newPhone = edPhoneUpdate.getText().toString().trim();
                String newType = selectedContactType;

                //image pas encore
                contact.setName(newNom);
                contact.setPhoneNumber1(newPhone);
                contact.setTypeContact(newType);
                ContactManager.update(getContext(),contact);
                updateContactList();
                Toast.makeText(getContext(), "Update successful", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void handleClickDeleteContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.create();
        builder.setTitle("Delete contacts");
        builder.setMessage("Are your sure ?");

        builder.setPositiveButton("Yes", (DialogInterface dialog, int which) ->{
            ContactManager.delete(getContext(), contact.getId());
            LinearLayout ll = (LinearLayout) getParent();
            ll.removeView(this);
            Toast.makeText(getContext(), "Delete contact", Toast.LENGTH_LONG).show();
        });

        builder.setNegativeButton("No", (DialogInterface dialog, int which) ->{

        });
        builder.show();
    }

    private void handleChangeImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change picture");
        builder.setItems(new CharSequence[]{"Take photos", "Choose picture"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        handleTakePhoto();
                        break;
                    case 1:
                        handleChooseFromGallery();
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancle", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //pas trouver solution
    private void handleTakePhoto(){
        Toast.makeText(getContext(), "Take photo", Toast.LENGTH_LONG).show();
    }

    private void handleChooseFromGallery(){
        Toast.makeText(getContext(), "Choose picture", Toast.LENGTH_LONG).show();

    }

    private void updateContactList(){
        removeAllViews();
        ContactManager.getAll(getContext());
    }
}

package com.example.contactmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactmanager.entitie.Contact;
import com.example.contactmanager.manager.ContactManager;
import com.example.contactmanager.service.ConnexionBd;

import java.util.ArrayList;
import java.util.List;

public class ListContactActivity extends AppCompatActivity {

    Context context;
    Button btnShowAll, btnShowAmis, btnShowFamilles, btnShowCollegues, btnAddContact, btnSave;
    SearchView svSearch;
    LinearLayout llShowListContact;
    ArrayList<Contact>contacts;

    String selectedContactType;

    ImageView ivImageProfile;
    EditText edName,edPhone1, edPhone2, edEmail;

    Spinner snTypeContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);
        context=this;
        llShowListContact=findViewById(R.id.ll_show_list_contact);
        btnShowAll=findViewById(R.id.btn_show_all_contact);
        btnShowAmis=findViewById(R.id.btn_show_amis_contact);
        btnShowFamilles=findViewById(R.id.btn_show_famille_contact);
        btnShowCollegues=findViewById(R.id.btn_show_collegues_contact);
        btnAddContact=findViewById(R.id.btn_add_contact);
        svSearch=findViewById(R.id.sv_search_contact);

        contacts= ContactManager.getAll(this);
        for (Contact contact:contacts){
            llShowListContact.addView(new ContactView(this, contact));
        }

        btnAddContact.setOnClickListener(v -> {
            handleClickAddContact();
        });

        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLayout();
            }
        });

        btnShowAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactAtType("Amis");
            }
        });

        btnShowFamilles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactAtType("Familles");
            }
        });

        btnShowCollegues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactAtType("Collègues");
            }
        });
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                llShowListContact.removeAllViews();
                contacts = ContactManager.searchContacts(context,newText);
                for(Contact contact : contacts){
                    llShowListContact.addView(new ContactView(context,contact));
                }
                return true;
            }
        });
        // Evenement sur LinearLayout par position pour appeler ou envoyer SMS
        llShowListContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenir les détails du contact correspondant à l'élément cliqué
                Contact contact = (Contact) v.getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(ListContactActivity.this);
                builder.setTitle("Choose an option")
                        .setMessage("What do you want to do with this contact?")
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String phoneNumber = contact.getPhoneNumber1();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneNumber));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Send message", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String phoneNumber = contact.getPhoneNumber1();
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("smsto:" + phoneNumber));
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("Cancel", null)
                        .show();
            }
        });

    }

    private void handleClickAddContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        LinearLayout layoutFormAddContact = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.ad_add_contact_layout, null);
        ivImageProfile = layoutFormAddContact.findViewById(R.id.iv_profile_image);
        edName = layoutFormAddContact.findViewById(R.id.ed_name_add_contact);
        edPhone1 = layoutFormAddContact.findViewById(R.id.ed_phone1_add_contact);
        edPhone2 = layoutFormAddContact.findViewById(R.id.ed_phone2_add_contact);
        edEmail = layoutFormAddContact.findViewById(R.id.ed_email_add_contact);
        snTypeContact = layoutFormAddContact.findViewById(R.id.sn_type_add_contact);
        btnSave=layoutFormAddContact.findViewById(R.id.btn_save_add_contact);

        List<String> contactTypes = new ArrayList<>();
        contactTypes.add("Amis");
        contactTypes.add("Familles");
        contactTypes.add("Collègues");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snTypeContact.setAdapter(adapter);
        snTypeContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedContactType = contactTypes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setView(layoutFormAddContact);
        dialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            Contact contact = new Contact();
            @Override
            public void onClick(View v) {
                //image pas encore
                String newNameContact = edName.getText().toString().trim();
                String newPhone1 = edPhone1.getText().toString().trim();
                String newPhone2 = edPhone2.getText().toString().trim();
                String newEmail = edEmail.getText().toString().trim();
                String newType = selectedContactType;

                //image pas encore
                contact.setName(newNameContact);
                contact.setPhoneNumber1(newPhone1);
                contact.setPhoneNumber2(newPhone2);
                contact.setEmail(newEmail);
                contact.setTypeContact(newType);
                ContactManager.addContact(context, contact);
                Toast.makeText(context, "save contact", Toast.LENGTH_LONG).show();
                updateLayout();
                dialog.dismiss();
            }
        });
        //image profil
        ivImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialogue pour choisir entre la caméra et la galerie
                AlertDialog.Builder builder = new AlertDialog.Builder(ListContactActivity.this);
                builder.setTitle("Choose a picture : ");
                String[] options = {"Camera", "Gallery"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            openCamera();
                        } else if (which == 1) {
                             openGallery();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(takePictureIntent);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivity(galleryIntent);
    }

    private void showContactAtType(String typeContactToUPdate){
        llShowListContact.removeAllViews();
        ArrayList<Contact>contacts = ContactManager.getByType(context, typeContactToUPdate);
        for (Contact contact:contacts){
            llShowListContact.addView(new ContactView(this, contact));
        }
    }

    private void updateLayout(){
        contacts= ContactManager.getAll(this);
        for (Contact contact:contacts){
            llShowListContact.addView(new ContactView(this, contact));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnexionBd.close();
    }
}
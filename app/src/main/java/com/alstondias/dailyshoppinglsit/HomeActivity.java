package com.alstondias.dailyshoppinglsit;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alstondias.dailyshoppinglsit.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;



public class HomeActivity extends AppCompatActivity {
    public  Toolbar toolbar;

    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daily Shopping List");

        mAuth =FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();

        String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);
        mDatabase.keepSynced(true);

        recyclerView = findViewById( R.id.recycler_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        fab_btn = findViewById(R.id.fab);

        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();

            }
        });

    }


    private void customDialog()
    {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

        View myview = inflater.inflate(R.layout.input_data,null);

        final AlertDialog dialog = mydialog.create();

        dialog.setView(myview);

        final EditText type = myview.findViewById(R.id.edt_type);
        final EditText amount = myview.findViewById(R.id.edt_amount);
        final EditText note = myview.findViewById(R.id.edt_note);
        Button btnSave = myview.findViewById(R.id.btn_save);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mtype =type.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String  mNote = note.getText().toString().trim();

                int amountint = Integer.parseInt(mAmount);
                if(TextUtils.isEmpty(mtype))
                {
                    type.setError("Required..");
                    return;
                }

                if(TextUtils.isEmpty(mAmount))
                {
                    amount.setError("Required..");
                    return;
                }
                if(TextUtils.isEmpty(mNote))
                {
                    note.setError("Required..");
                    return;
                }

                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(mtype,amountint,mNote,date,id);

                mDatabase.child(id).setValue(data);
                Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_LONG).show();



                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
        }

        public  void setType(String type)
        {
            TextView mType= myview.findViewById(R.id.type);
            mType.setText(type);
        }

        public void setNote(String note)
        {
            TextView mNote = myview.findViewById(R.id.note);
            mNote.setText(note);
        }

        public void setDate(String date)
        {
            TextView mDate = myview.findViewById(R.id.date);
            mDate.setText(date);
        }

        public void setAmount(int amount)
        {
            TextView mAmount = myview.findViewById(R.id.amount);
            String stam = String.valueOf(mAmount);
            mAmount.setText(stam);
        }

    }
}

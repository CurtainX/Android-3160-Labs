package com.example.musfiqrahman.waitlist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    GuestListAdapter mAdapter;
    Button btn;
    EditText guestNameInput, guestNumInput;
    List<GuestInfo> guestIn;
    GuestDBAdapter guestDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.all_guest_list_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        guestIn = new ArrayList<>();
        btn=(Button)findViewById(R.id.add_to_waitlist_button);
        guestDBAdapter=new GuestDBAdapter(this);
        guestDBAdapter.open();
        Cursor cursor=guestDBAdapter.getAllRows();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            //Important: get the first record from table
            String guestname2=cursor.getString(cursor.getColumnIndex(GuestDBContractor.DB_GUEST_NAME));
            int guest_num2=cursor.getInt(cursor.getColumnIndex(String.valueOf(GuestDBContractor.DB_GUEST_NUMBER)));
            GuestInfo guestInfo2 = new GuestInfo(guestname2, guest_num2);
            guestIn.add(guestInfo2);
            //-------------------------
            while(cursor.moveToNext()){
                String guestname=cursor.getString(cursor.getColumnIndex(GuestDBContractor.DB_GUEST_NAME));
                int guest_num=cursor.getInt(cursor.getColumnIndex(String.valueOf(GuestDBContractor.DB_GUEST_NUMBER)));
                GuestInfo guestInfo = new GuestInfo(guestname, guest_num);
                guestIn.add(guestInfo);
                //Toast.makeText(view.getContext(),guestname+" "+id+" ",Toast.LENGTH_LONG).show();
            }
        };
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int guestNo = 0;
                guestNameInput = (EditText) findViewById(R.id.guest_name);
                guestNumInput = (EditText) findViewById(R.id.num_of_guest);
                String name = String.valueOf(guestNameInput.getText());
                String guestNum = String.valueOf(guestNumInput.getText());
                guestNameInput.setText("");
                guestNumInput.setText("");
                if (name.length() <= 0 || guestNum.length() <= 0) {
                    Toast.makeText(view.getContext(), "Please Enter Guest Name and Guest Number.", Toast.LENGTH_LONG).show();
                } else {
                        guestNo = Integer.parseInt(guestNum);
                        if(guestNo>0){
                            GuestInfo guestInfo = new GuestInfo(name, guestNo);
                            guestIn.add(guestInfo);
                            Toast.makeText(view.getContext(), "Add success!", Toast.LENGTH_LONG).show();
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                            guestDBAdapter.insertRow(name, Integer.parseInt(guestNum));
//                            Cursor cursor=guestDBAdapter.getAllRows();
//                            if(cursor.moveToFirst()){
//                                cursor.moveToFirst();
//                                while(cursor.moveToNext()){
//                                    String guestname=cursor.getString(cursor.getColumnIndex(GuestDBContractor.DB_GUEST_NAME));
//                                    int guest_num=cursor.getInt(cursor.getColumnIndex(String.valueOf(GuestDBContractor.DB_GUEST_NUMBER)));
//                                    GuestInfo guestInfo = new GuestInfo(guestname, guest_num);
//                                    guestIn.add(guestInfo);
//                                    //Toast.makeText(view.getContext(),guestname+" "+id+" ",Toast.LENGTH_LONG).show();
//                                }
//                            }

                        }
                        else {
                            Toast.makeText(view.getContext(), "Guest Number Cannot Be Zero.", Toast.LENGTH_LONG).show();
                        }
                }
            }
            });
        mAdapter = new GuestListAdapter(guestIn);
        recyclerView.setAdapter(mAdapter);
    }

}

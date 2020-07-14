package com.hanium.cctv.function;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hanium.cctv.R;
import com.hanium.cctv.model.cctv;

public class list_of_cctv extends AppCompatActivity {
    View addlistView;
    EditText text_cctvid,text_cctvpw,text_name,text_place,text_special;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_cctv);

        final cctv cctv = new cctv();


        addlistView = (View) View.inflate(list_of_cctv.this,R.layout.addwork_cctvlist,null);
        FloatingActionButton add_cctvlist = (FloatingActionButton) findViewById(R.id.fab_add_cctvlist);

        text_cctvid = (EditText) findViewById(R.id.text_cctvlist_cctvid);
        text_cctvpw = (EditText) findViewById(R.id.text_cctvlist_cctvpw);
        text_name = (EditText) findViewById(R.id.text_cctvlist_name);
        text_place = (EditText) findViewById(R.id.text_cctvlist_place);
        text_special = (EditText) findViewById(R.id.text_cctvlist_special);
        add_cctvlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(list_of_cctv.this);
                dlg.setTitle("cctv 추가하기");
                dlg.setView(addlistView);
                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cctv.setId(text_cctvid.getText().toString());
                        cctv.setPw(text_cctvpw.getText().toString());
                        cctv.setName(text_name.getText().toString());
                        cctv.setPlace(text_place.getText().toString());
                        cctv.setSpecial(text_special.getText().toString());//sqlite에 집어넣어야함
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
            }
        });
    }
}
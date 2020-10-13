package com.hanium.cctv.record;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hanium.cctv.R;
import com.hanium.cctv.others.DbHelper;


public class RecordActivity extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private RECORDAdapter adapter;
    private DbHelper db;
    private ActionBar record_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initAll();

        Toolbar record_toolbar = (Toolbar) findViewById(R.id.record_toolbar);
        setSupportActionBar(record_toolbar);
        record_actionbar = getSupportActionBar();
        record_actionbar.setDisplayShowCustomEnabled(true);
        record_actionbar.setDisplayShowTitleEnabled(true);
        record_actionbar.setTitle(R.string.record_actionbar);
        record_actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void initAll() {
        setupAdapter();
        setupItemClick();
    }

    private void setupAdapter() {
        db = new DbHelper(context);
        listView = (ListView) findViewById(R.id.listview_recordlist);
        adapter = new RECORDAdapter(RecordActivity.this, R.layout.listview_recordlist_adapter, db.getRECORDLIST());
        listView.setAdapter(adapter);
    }

    private void setupItemClick() {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(RecordActivity.this);
                dlg.setMessage("기록을 삭제하시겠습니까?");
                dlg.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteRECORDLISTById(adapter.getItem(position));
                        adapter.getRecordlist().remove(position);
                        db.updateRECORDLIST(adapter.getRecord());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "삭제됨.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setNegativeButton("아니오", null);

                AlertDialog alertDialog = dlg.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.blank_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.hanium.cctv.record;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hanium.cctv.R;
import com.hanium.cctv.others.DbHelper;

import java.util.ArrayList;

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
        setupListViewMultiSelect();
    }

    private void setupAdapter() {
        db = new DbHelper(context);
        listView = (ListView) findViewById(R.id.listview_recordlist);
        adapter = new RECORDAdapter(RecordActivity.this, R.layout.listview_recordlist_adapter, db.getRECORDLIST());
        listView.setAdapter(adapter);
    }

    private void setupListViewMultiSelect() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + "개 선택됨");
                if (checkedCount == 0) mode.finish();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.toolbar_action_mode, menu);
                record_actionbar.hide();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        ArrayList<record> removelist = new ArrayList<>();
                        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                        for (int i = 0; i < checkedItems.size(); i++) {
                            int key = checkedItems.keyAt(i);
                            if (checkedItems.get(key)) {
                                db.deleteRECORDLISTById(adapter.getItem(key));
                                removelist.add(adapter.getRecordlist().get(key));
                            }
                        }
                        adapter.getRecordlist().removeAll(removelist);
                        db.updateRECORDLIST(adapter.getRecord());
                        adapter.notifyDataSetChanged();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                record_actionbar.show();
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
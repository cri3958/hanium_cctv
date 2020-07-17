package com.hanium.cctv.function;

import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hanium.cctv.CCTVAdapter;
import com.hanium.cctv.R;
import com.hanium.cctv.util.DbHelper;

public class activity_list_of_cctv extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private CCTVAdapter adapter;
    private DbHelper db;
    private int listposition = 0;

    private com.hanium.cctv.model.cctv cctv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_cctv);

        initAll();
    }

    private void initAll() {
//        setupAdapter();
        setupListViewMultiSelect();
        setupCustomDialog();
    }

    private void setupAdapter() {
        db = new DbHelper(context);
        listView = findViewById(R.id.listview_cctvlist);
        adapter = new CCTVAdapter(activity_list_of_cctv.this, listView, R.layout.cctvlist_item_gridview, db.getCCTVLIST());
        listView.setAdapter(adapter);
    }

    private void setupListViewMultiSelect() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                listposition = position;
                final int checkedCount = listView.getCheckedItemCount();
                FloatingActionButton btn_del_cctvlist = findViewById(R.id.fab_del_cctvlist);
                FloatingActionButton btn_add_cctvlist = findViewById(R.id.fab_add_cctvlist);
                if (checkedCount == 0) {
                    btn_del_cctvlist.setVisibility(View.INVISIBLE);
                    btn_add_cctvlist.setVisibility(View.VISIBLE);
                } else {
                    btn_del_cctvlist.setVisibility(View.VISIBLE);
                    btn_add_cctvlist.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    private void setupCustomDialog() {
        final View cctvLayout = getLayoutInflater().inflate(R.layout.addwork_cctvlist, null);
        DialogHelper.getAddcctvlistDialog(activity_list_of_cctv.this, cctvLayout, adapter);
    }
}

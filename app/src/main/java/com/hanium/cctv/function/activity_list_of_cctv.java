package com.hanium.cctv.function;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanium.cctv.CCTVAdapter;
import com.hanium.cctv.R;
import com.hanium.cctv.model.cctv;
import com.hanium.cctv.util.DbHelper;

import java.util.ArrayList;

public class activity_list_of_cctv extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private CCTVAdapter adapter;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_cctv);

        initAll();
    }

    private void initAll() {
        setupAdapter();
        setupListViewMultiSelect();
        setupCustomDialog();
    }

    private void setupAdapter() {
        db = new DbHelper(context);
        listView = (ListView) findViewById(R.id.listview_cctvlist);
        adapter = new CCTVAdapter(activity_list_of_cctv.this, listView, R.layout.listview_cctvlist_adapter, db.getCCTVLIST());
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
                        ArrayList<cctv> removelist = new ArrayList<>();
                        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                        for (int i = 0; i < checkedItems.size(); i++) {
                            int key = checkedItems.keyAt(i);
                            if (checkedItems.get(key)) {
                                db.deleteCCTVLISTById(adapter.getItem(key));
                                removelist.add(adapter.getCctvlist().get(key));
                            }
                        }
                        adapter.getCctvlist().removeAll(removelist);
                        db.updateCCTVLIST(adapter.getCctv());
                        adapter.notifyDataSetChanged();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
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

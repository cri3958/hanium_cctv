package com.hanium.cctv.cctv;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hanium.cctv.R;

import java.util.ArrayList;

public class CctvActivity extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private CCTVAdapter adapter;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);

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
        adapter = new CCTVAdapter(CctvActivity.this, listView, R.layout.listview_cctvlist_adapter, db.getCCTVLIST());
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
        DialogHelper.getAddcctvlistDialog(CctvActivity.this, cctvLayout, adapter);
    }

    public static class DialogHelper {

        public static void getAddcctvlistDialog(final Activity activity, final View cctvLayout, final CCTVAdapter adapter) {
            final cctv cctv = new cctv();

            FloatingActionButton add_cctvlist = activity.findViewById(R.id.fab_add_cctvlist);

            final EditText text_cctvnum = cctvLayout.findViewById(R.id.text_cctvlist_cctvnum);
            final EditText text_cctvpw = cctvLayout.findViewById(R.id.text_cctvlist_cctvpw);
            final EditText text_name = cctvLayout.findViewById(R.id.text_cctvlist_name);
            final EditText text_place = cctvLayout.findViewById(R.id.text_cctvlist_place);
            final EditText text_special = cctvLayout.findViewById(R.id.text_cctvlist_special);
            final TextView save = cctvLayout.findViewById(R.id.btn_cctvlist_save);
            final TextView cancel = cctvLayout.findViewById(R.id.btn_cctvlist_cancel);

            AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
            //dlg.setTitle("cctv 추가하기");
            dlg.setView(cctvLayout);
            dlg.setCancelable(false);
            final AlertDialog dialog = dlg.create();

            add_cctvlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (true) { //번호랑 비번 검사
                        DbHelper dbHelper = new DbHelper(activity);
                        cctv.setNumber(text_cctvnum.getText().toString());
                        cctv.setPw(text_cctvpw.getText().toString());
                        cctv.setName(text_name.getText().toString());
                        cctv.setPlace(text_place.getText().toString());
                        cctv.setSpecial(text_special.getText().toString());
                        dbHelper.insertCCTVLIST(cctv);

                        adapter.clear();
                        adapter.addAll(dbHelper.getCCTVLIST());
                        adapter.notifyDataSetChanged();

                        text_cctvnum.getText().clear();
                        text_cctvpw.getText().clear();
                        text_name.getText().clear();
                        text_place.getText().clear();
                        text_special.getText().clear();
                        text_cctvnum.requestFocus();
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}

package com.hanium.cctv.cctv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hanium.cctv.R;
import com.hanium.cctv.others.DbHelper;
import com.hanium.cctv.others.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class CctvActivity extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private CCTVAdapter adapter;
    private DbHelper db;
    private ActionBar cctv_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);
        initAll();

        Toolbar cctv_toolbar = findViewById(R.id.cctv_toolbar);
        setSupportActionBar(cctv_toolbar);
        cctv_actionbar = getSupportActionBar();
        cctv_actionbar.setDisplayShowCustomEnabled(true);
        cctv_actionbar.setDisplayShowTitleEnabled(true);
        cctv_actionbar.setTitle(R.string.cctv_actionbar);
        cctv_actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void initAll() {
        setupAdapter();
        setupItemClick();
        setupCustomDialog();
    }

    private void setupAdapter() {
        db = new DbHelper(context);
        listView = (ListView) findViewById(R.id.listview_cctvlist);
        adapter = new CCTVAdapter(CctvActivity.this, R.layout.listview_cctvlist_adapter, db.getCCTVLIST());
        listView.setAdapter(adapter);
    }


    private void setupItemClick(){  //야호!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), cctv_watch_normal.class);
                intent.putExtra("object_num", adapter.getItem(position).getNumber());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(CctvActivity.this, R.style.MyAlertDialogStyle);
                dlg.setTitle("Really want delete cctv?");
                dlg.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        db.deleteCCTVLISTById(adapter.getItem(position));
                        adapter.getCctvlist().remove(position);
                        db.updateCCTVLIST(adapter.getCctv());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setNegativeButton("NO", null);

                final AlertDialog alertDialog = dlg.create();
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
                    }
                });
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                alertDialog.show();
                return true;
            }
        });
    }

    private void setupCustomDialog() {
        final View cctvLayout = getLayoutInflater().inflate(R.layout.addwork_cctvlist, null);
        DialogHelper.getAddcctvlistDialog(CctvActivity.this, cctvLayout, adapter);
    }

    public static class DialogHelper {
        private static boolean check = false;
        private static EditText text_cctvnum,text_cctvpw,text_name,text_place,text_special;

        public static void getAddcctvlistDialog(final Activity activity, final View cctvLayout, final CCTVAdapter adapter) {
            final cctv cctv = new cctv();

            FloatingActionButton add_cctvlist = activity.findViewById(R.id.fab_add_cctvlist);

            text_cctvnum = cctvLayout.findViewById(R.id.text_cctvlist_cctvnum);
            text_cctvpw = cctvLayout.findViewById(R.id.text_cctvlist_cctvpw);
            text_name = cctvLayout.findViewById(R.id.text_cctvlist_name);
            text_place = cctvLayout.findViewById(R.id.text_cctvlist_place);
            text_special = cctvLayout.findViewById(R.id.text_cctvlist_special);
            TextView save = cctvLayout.findViewById(R.id.btn_cctvlist_save);
            TextView cancel = cctvLayout.findViewById(R.id.btn_cctvlist_cancel);

            AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
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
                    text_cctvnum.getText().clear();
                    text_cctvpw.getText().clear();
                    text_name.getText().clear();
                    text_place.getText().clear();
                    text_special.getText().clear();
                    text_cctvnum.setEnabled(true);
                    text_cctvpw.setEnabled(true);
                    check = false;
                    text_cctvnum.requestFocus();
                    dialog.dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    check = false;

                    if (text_cctvnum.getText().toString().isEmpty() || text_cctvpw.getText().toString().isEmpty() || text_name.getText().toString().isEmpty() || text_place.getText().toString().isEmpty()) {
                        Toast.makeText(activity.getApplicationContext(), "정보를 입력해주십시오.", Toast.LENGTH_SHORT).show();
                    } else {
                        String object_num = text_cctvnum.getText().toString();
                        String object_pw = text_cctvpw.getText().toString();
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {  //id와 pw가 서버와 일치
                                        if (text_special.getText().toString().isEmpty()) {
                                            text_special.setText("특이사항 없음");
                                        }
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
                                        text_cctvnum.setEnabled(true);
                                        text_cctvpw.setEnabled(true);
                                        check = false;
                                        text_cctvnum.requestFocus();
                                        dialog.dismiss();
                                    } else {    //id와 pw가 서버와 불일치
                                        Toast.makeText(activity.getApplicationContext(), "CCTV ID와 PW를 확인해주십시오.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        CctvcheckRequest cctvcheckRequest = new CctvcheckRequest(object_num, object_pw, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
                        queue.add(cctvcheckRequest);
                    }
                }
            });
        }
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
                Intent intent = new Intent(CctvActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

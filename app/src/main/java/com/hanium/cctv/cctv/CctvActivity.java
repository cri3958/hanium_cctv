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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CctvActivity extends AppCompatActivity {
    private Context context = this;
    private ListView listView;
    private CCTVAdapter adapter;
    private DbHelper db;
    private ActionBar cctv_actionbar;
    private ImageView action_delete;

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

        action_delete = findViewById(R.id.cctv_action_delete);

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
                AlertDialog.Builder dlg = new AlertDialog.Builder(CctvActivity.this);
                dlg.setMessage("CCTV를 삭제하시겠습니까?");
                dlg.setPositiveButton("네", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        db.deleteCCTVLISTById(adapter.getItem(position));
                        adapter.getCctvlist().remove(position);
                        db.updateCCTVLIST(adapter.getCctv());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"삭제됨.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setNegativeButton("아니오", null);

                AlertDialog alertDialog = dlg.create();
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
        private static Button btn_cctvpass;

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
            btn_cctvpass = cctvLayout.findViewById(R.id.btn_cctvpass);

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
                    btn_cctvpass.setText("확인하기");
                    text_cctvnum.setEnabled(true);
                    text_cctvpw.setEnabled(true);
                    btn_cctvpass.setEnabled(true);
                    check = false;
                    text_cctvnum.requestFocus();
                    dialog.dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (text_cctvnum.getText().toString().isEmpty() || text_cctvpw.getText().toString().isEmpty() || text_name.getText().toString().isEmpty() || text_place.getText().toString().isEmpty()) {
                        Toast.makeText(activity.getApplicationContext(), "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else if (!(check)) {
                        Toast.makeText(activity.getApplicationContext(), "번호와 비밀번호를 확인 해야합니다.", Toast.LENGTH_SHORT).show();
                    } else if (check) { //번호랑 비번 검사
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
                        btn_cctvpass.setEnabled(true);
                        check = false;
                        text_cctvnum.requestFocus();
                        dialog.dismiss();
                    }
                }
            });
            btn_cctvpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(text_cctvnum.getText().toString().isEmpty()) && !(text_cctvpw.getText().toString().isEmpty())) {
                        String object_num = text_cctvnum.getText().toString();
                        String object_pw = text_cctvpw.getText().toString();
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(activity.getApplicationContext(), "확인되었습니다.", Toast.LENGTH_SHORT).show();
                                        btn_cctvpass.setText("확인됨");
                                        text_cctvnum.setEnabled(false);
                                        text_cctvpw.setEnabled(false);
                                        btn_cctvpass.setEnabled(false);
                                        check = true;
                                    } else {
                                        Toast.makeText(activity.getApplicationContext(), "정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        CctvcheckRequest cctvcheckRequest = new CctvcheckRequest(object_num, object_pw, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
                        queue.add(cctvcheckRequest);
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "cctv번호와 비밀번호를 적은 후 눌러주십시오.", Toast.LENGTH_SHORT).show();
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.hanium.cctv.function;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hanium.cctv.CCTVAdapter;
import com.hanium.cctv.R;
import com.hanium.cctv.model.cctv;
import com.hanium.cctv.util.DbHelper;

public class DialogHelper {

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
        dlg.setTitle("cctv 추가하기");
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

package com.hanium.cctv.function;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hanium.cctv.CCTVAdapter;
import com.hanium.cctv.R;
import com.hanium.cctv.model.cctv;
import com.hanium.cctv.util.DbHelper;

import java.util.List;

public class list_of_cctv extends AppCompatActivity {
    View addlistView;
    EditText text_cctvnum,text_cctvpw,text_name,text_place,text_special;
    CCTVAdapter cctvAdapter;
    ListView listView;

    private cctv cctv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_of_cctv);

        cctv = new cctv();
        MakeCCTVlist();
        addlistView = (View) View.inflate(list_of_cctv.this,R.layout.addwork_cctvlist,null);
        FloatingActionButton add_cctvlist = (FloatingActionButton) findViewById(R.id.fab_add_cctvlist);

        text_cctvnum = (EditText) addlistView.findViewById(R.id.text_cctvlist_cctvnum);
        text_cctvpw = (EditText) addlistView.findViewById(R.id.text_cctvlist_cctvpw);
        text_name = (EditText) addlistView.findViewById(R.id.text_cctvlist_name);
        text_place = (EditText) addlistView.findViewById(R.id.text_cctvlist_place);
        text_special = (EditText) addlistView.findViewById(R.id.text_cctvlist_special);//db까진 저장 완룓함
        add_cctvlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(list_of_cctv.this);
                dlg.setTitle("cctv 추가하기");
                dlg.setView(addlistView);
                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(true) {
                            cctv.setNumber(text_cctvnum.getText().toString());
                            cctv.setPw(text_cctvpw.getText().toString());
                            cctv.setName(text_name.getText().toString());
                            cctv.setPlace(text_place.getText().toString());
                            cctv.setSpecial(text_special.getText().toString());

                            DbHelper dbHelper = new DbHelper(list_of_cctv.this);
                            dbHelper.insertCCTVLIST(cctv);
                            MakeCCTVlist();

                        }

                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
            }
        });
    }

    private void MakeCCTVlist(){
        listView = (ListView) findViewById(R.id.listview_cctvlist);


    }

    /*public class CCTVAdapter extends BaseAdapter{
        private final List<String> list;
        private final LayoutInflater inflater;

        public CCTVAdapter(Context context, List<String> list){
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CCTVViewHolder holder = null;
            cctv = new cctv();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.cctvlist_item_gridview, parent, false);
                holder = new CCTVViewHolder();
                holder.cctvlistName = (TextView) convertView.findViewById(R.id.cctvlist_item_name);
                holder.cctvlistNum = (TextView) convertView.findViewById(R.id.cctvlist_item_num);
                holder.cctvlistInfo = (ImageView) convertView.findViewById(R.id.cctvlist_item_info);
                holder.cctvlistPlay = (ImageView) convertView.findViewById(R.id.cctvlist_item_play);
                convertView.setTag(holder);
            } else
                holder = (CCTVViewHolder) convertView.getTag();

            holder.cctvlistName.setText(cctv.getName());
            holder.cctvlistNum.setText(cctv.getNumber());

            //info와 play눌렀을때의 동작 만들어야함.

            return convertView;
        }

        private class CCTVViewHolder{
            TextView cctvlistNum,cctvlistName;
            ImageView cctvlistInfo, cctvlistPlay;
        }
    }*/

}
package com.hanium.cctv;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hanium.cctv.model.cctv;

import java.util.ArrayList;
import java.util.Objects;

public class CCTVAdapter extends ArrayAdapter<cctv> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<cctv> cctvlist;
    private cctv cctv;
    private ListView mListView;

    private class CCTVViewHolder{
        TextView cctvlistNum,cctvlistName;
        ImageView cctvlistInfo, cctvlistPlay;
    }

    public CCTVAdapter(Activity activity, ListView listView,  int resource, ArrayList<cctv> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mListView = listView;
        mResource = resource;
        cctvlist = objects;
        Log.d("@@@@", "bf in get VIEW");
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String Num = Objects.requireNonNull(getItem(position)).getNumber();
        String pw = Objects.requireNonNull(getItem(position)).getPw();
        String Name = Objects.requireNonNull(getItem(position)).getName();
        String Place= Objects.requireNonNull(getItem(position)).getPlace();
        String Special = Objects.requireNonNull(getItem(position)).getSpecial();
        Log.d("@@@", "in getViewwwwwwww!");//왜 안들어올까?
        cctv = new cctv(Num, pw, Name, Place, Special);

        final CCTVViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
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

        holder.cctvlistInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.cctvlistPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return cctvlist.size();
    }

    @Override
    public com.hanium.cctv.model.cctv getItem(int position) {
        return cctvlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public ArrayList<cctv> getCctvlist() { return cctvlist; }

    public cctv getCctv() { return cctv; }


}
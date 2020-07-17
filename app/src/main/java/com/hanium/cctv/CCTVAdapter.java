package com.hanium.cctv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanium.cctv.function.list_of_cctv;
import com.hanium.cctv.model.cctv;

import java.util.List;

public class CCTVAdapter extends BaseAdapter {
    private final List<String> list;
    private final LayoutInflater inflater;
    private cctv cctv;
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
}
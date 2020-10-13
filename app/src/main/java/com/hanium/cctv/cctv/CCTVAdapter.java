package com.hanium.cctv.cctv;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hanium.cctv.R;

import java.util.ArrayList;
import java.util.Objects;

public class CCTVAdapter extends ArrayAdapter<cctv> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<cctv> cctvlist;
    private cctv cctv;

    public CCTVAdapter(Activity activity, int resource, ArrayList<cctv> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mResource = resource;
        cctvlist = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String Num = Objects.requireNonNull(getItem(position)).getNumber();
        String pw = Objects.requireNonNull(getItem(position)).getPw();
        String Name = Objects.requireNonNull(getItem(position)).getName();
        String Place= Objects.requireNonNull(getItem(position)).getPlace();
        String Special = Objects.requireNonNull(getItem(position)).getSpecial();

        cctv = new cctv(Num, pw, Name, Place, Special);

        final CCTVViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new CCTVViewHolder();
            holder.cctvlist_Name = convertView.findViewById(R.id.cctvlist_item_name);
            holder.cctvlist_Num = convertView.findViewById(R.id.cctvlist_item_num);
            holder.cctvlist_Place = convertView.findViewById(R.id.cctvlist_item_place);
            holder.cctvlist_Special = convertView.findViewById(R.id.cctvlist_item_speical);
            convertView.setTag(holder);
        } else
            holder = (CCTVViewHolder) convertView.getTag();

        holder.cctvlist_Name.setText(cctv.getName());
        holder.cctvlist_Num.setText(cctv.getNumber());
        holder.cctvlist_Place.setText(cctv.getPlace());
        holder.cctvlist_Special.setText(cctv.getSpecial());

        return convertView;
    }

    @Override
    public cctv getItem(int position) {
        return cctvlist.get(position);
    }

    @Override
    public int getCount() {
        return cctvlist.size();
    }

    private static class CCTVViewHolder {
        TextView cctvlist_Num, cctvlist_Name, cctvlist_Place, cctvlist_Special;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<cctv> getCctvlist() {
        return cctvlist;
    }

    public cctv getCctv() {
        return cctv;
    }



}
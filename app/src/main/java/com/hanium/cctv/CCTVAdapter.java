package com.hanium.cctv;

import android.app.Activity;
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

    public CCTVAdapter(Activity activity, ListView listView,  int resource, ArrayList<cctv> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mListView = listView;
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
            holder.cctvlistName = (TextView) convertView.findViewById(R.id.cctvlist_item_name);
            holder.cctvlistNum = (TextView) convertView.findViewById(R.id.cctvlist_item_num);
            holder.cctvlistPlay = (ImageView) convertView.findViewById(R.id.cctvlist_item_play);
            holder.cctvlistPlace = (TextView) convertView.findViewById(R.id.cctvlist_item_place);
            holder.cctvlistSpecial = (TextView) convertView.findViewById(R.id.cctvlist_item_speical);
            convertView.setTag(holder);
        } else
            holder = (CCTVViewHolder) convertView.getTag();

        holder.cctvlistName.setText(cctv.getName());
        holder.cctvlistNum.setText(cctv.getNumber());
        holder.cctvlistPlace.setText(cctv.getPlace());
        holder.cctvlistSpecial.setText(cctv.getSpecial());

        holder.cctvlistPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cctv.getNumber() 넘겨주면서 동영상 액티비티 시작
            }
        });
        return convertView;
    }

    private class CCTVViewHolder {
        TextView cctvlistNum, cctvlistName, cctvlistPlace, cctvlistSpecial;
        ImageView cctvlistPlay;
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
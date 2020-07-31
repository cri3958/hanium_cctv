package com.hanium.cctv.record;

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

public class RECORDAdapter extends ArrayAdapter<record> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<record> recordlist;
    private record record;

    public RECORDAdapter(Activity activity, int resource, ArrayList<record> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mResource = resource;
        recordlist = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String date = Objects.requireNonNull(getItem(position)).getDate();
        String object_num = Objects.requireNonNull(getItem(position)).getObject_num();
        String mem_name = Objects.requireNonNull(getItem(position)).getMem_name();
        String reason = Objects.requireNonNull(getItem(position)).getReason();

        record = new record(date, object_num, mem_name, reason);

        final RECORDViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new RECORDViewHolder();
            holder.recordlist_date = convertView.findViewById(R.id.recordlist_item_date);
            holder.recordlist_object_num = convertView.findViewById(R.id.recordlist_item_object_num);
            holder.recordlist_mem_name = convertView.findViewById(R.id.recordlist_item_mem_name);
            holder.recordlist_reason = convertView.findViewById(R.id.recordlist_item_reason);
            convertView.setTag(holder);
        } else
            holder = (RECORDViewHolder) convertView.getTag();

        holder.recordlist_date.setText(record.getDate());
        holder.recordlist_object_num.setText(record.getObject_num());
        holder.recordlist_mem_name.setText(record.getMem_name());
        holder.recordlist_reason.setText(record.getReason());

        return convertView;
    }

    @Override
    public int getCount() {
        return recordlist.size();
    }

    @Override
    public record getItem(int position) {
        return recordlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<record> getRecordlist() {
        return recordlist;
    }

    public record getRecord() {
        return record;
    }

    private class RECORDViewHolder {
        TextView recordlist_date, recordlist_object_num, recordlist_mem_name, recordlist_reason;
    }


}
package com.example.switchwifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nguyennam on 11/23/16.
 */

public class ListWifiAdapter extends BaseAdapter {

    private HashMap<String, Integer> listWifi;
    private LayoutInflater layoutInflater;
    private List<String> listSSID;
    private List<Integer> listLevel;

    public ListWifiAdapter(Context context, List<String> listSSID, List<Integer> listLevel) {
        this.listWifi = listWifi;
        this.listLevel = listLevel;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listWifi.size();
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.adapter_list_dialog, null);
            holder = new ViewHolder();
            holder.tvSSID = (TextView) view.findViewById(R.id.tvSSID);
            holder.imgLevel = (ImageView) view.findViewById(R.id.imgLevel);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvSSID.setText(listSSID.get(i));
        holder.imgLevel.setImageResource(R.drawable.ic_signal_wifi_0_bar);
        return view;
    }

    static class ViewHolder {
        TextView tvSSID;
        ImageView imgLevel;
    }

}

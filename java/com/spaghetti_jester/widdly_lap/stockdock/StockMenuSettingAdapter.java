package com.spaghetti_jester.widdly_lap.stockdock;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spaghetti_jester.widdly_lap.stockdock.StockMenuSetting;

/**
 * Created by Widdly-Lap on 4/12/2016.
 */
public class StockMenuSettingAdapter extends ArrayAdapter<StockMenuSetting> {

    Context context;
    int layoutResourceId;
    StockMenuSetting data[] = null;

    public StockMenuSettingAdapter(Context context, int layoutResourceId, StockMenuSetting[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SettingHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.stocklist_layout, parent, false);

            holder = new SettingHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.t1);
            holder.txtDesc = (TextView)row.findViewById(R.id.t2);
            holder.txtPrice = (TextView)row.findViewById(R.id.t3);


            row.setTag(holder);
        }
        else
        {
            holder = (SettingHolder)row.getTag();
        }

        StockMenuSetting setting = data[position];
        holder.txtTitle.setText(setting.myTitle);
        holder.txtDesc.setText(setting.description);
        holder.txtPrice.setText(setting.price);


        return row;
    }

    static class SettingHolder
    {
        TextView txtDesc;
        TextView txtTitle;
        TextView txtPrice;
    }
}
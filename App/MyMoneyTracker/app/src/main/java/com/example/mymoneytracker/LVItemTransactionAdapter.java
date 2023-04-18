package com.example.mymoneytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LVItemTransactionAdapter extends ArrayAdapter<LVItemTransaction> {
    private final Context context;
    private final ArrayList<LVItemTransaction> items;

    public LVItemTransactionAdapter(Context context, ArrayList<LVItemTransaction> items){
        super(context, R.layout.layout_lv_transaction, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_lv_transaction, parent, false);
        TextView transacDescription = (TextView) rowView.findViewById(R.id.tvLViewDescription);
        TextView transacAmount = (TextView) rowView.findViewById(R.id.tvLViewAmount);
        TextView transacCategory = (TextView) rowView.findViewById(R.id.tvLViewCategory);
        TextView transacDate = (TextView) rowView.findViewById(R.id.tvLViewDate);
        ImageView transacImage = (ImageView) rowView.findViewById(R.id.ivLView);
        TextView transacId = (TextView) rowView.findViewById(R.id.tvLViewTransactionId);

        transacDescription.setText(items.get(position).getDescription());
        transacAmount.setText("" + items.get(position).getAmount());
        transacCategory.setText(items.get(position).getCategory());
        Date date = new Date(items.get(position).getDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
        transacDate.setText(dateFormat.format(date));
        transacImage.setImageResource(items.get(position).getImage());
        transacId.setText("" + items.get(position).getTransacId());

        return rowView;
    }
}

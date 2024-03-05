package com.otto.mart.ui.fragment.olshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.olshop.Variant;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    private Context context;
    private List<Variant> variants;
    public int viewPos;

    public SpinnerAdapter(Context context, List<Variant> variants, int viewPos) {
        this.context = context;
        this.variants = variants;
        this.viewPos = viewPos;
    }

    @Override
    public int getCount() {
        return variants.size();
    }

    @Override
    public Variant getItem(int position) {
        return variants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = createView(position, convertView, parent);

        return view;
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_spinner, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.name);
        textView.setText(variants.get(position).getValue());

        return convertView;
    }

    public interface SpinnerListener {
        void onItemSelected(int position, Variant variant, int viewPosition);
    }
}

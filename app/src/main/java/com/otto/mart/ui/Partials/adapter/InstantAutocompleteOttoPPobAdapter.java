package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class InstantAutocompleteOttoPPobAdapter extends BaseAdapter implements Filterable {

    private Context gContext;
    List<CategoryModel> models;
    private List<CategoryModel> filteredModels;
    private ItemFilter mFilter = new ItemFilter();


    public InstantAutocompleteOttoPPobAdapter(Context gContext, List<CategoryModel> models) {
        this.gContext = gContext;
        this.models = models;
        filteredModels = models;
    }

    public void updateModel() {
        this.filteredModels = this.models;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredModels.size();
    }

    @Override
    public String getItem(int position) {
        return filteredModels.get(position).getTitle();
    }

    public CategoryModel getModel(int position) {
        return filteredModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) gContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.item_filter_subcategory, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.tvTitle = convertView.findViewById(R.id.category);
        convertView.setTag(viewHolder);

        String item = filteredModels.get(position).getTitle();
        if (!item.isEmpty() || viewHolder != null) {
            viewHolder.tvTitle.setText(item);
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView tvTitle;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            int count = models.size();
            final List<CategoryModel> tempItems = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                if (models.get(i).getTitle().toLowerCase().contains(filterString)) {
                    tempItems.add(models.get(i));
                }
            }

            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredModels = (ArrayList<CategoryModel>) results.values;
            notifyDataSetChanged();
        }
    }

}



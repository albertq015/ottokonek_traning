package com.otto.mart.ui.Partials.adapter.topUpEmoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.topUpEmoney.TopUpEmoneyProductResponseModel;

import java.util.List;

public class TopUpWalletAdapter extends BaseAdapter {
		Context context;
		List<TopUpEmoneyProductResponseModel.Data.Denomination> data;
		LayoutInflater inflter;

		public TopUpWalletAdapter(Context context, List<TopUpEmoneyProductResponseModel.Data.Denomination> data) {
			this.context = context;
			this.data = data;
			inflter = (LayoutInflater.from(context));
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup viewGroup) {
			view = inflter.inflate(R.layout.item_spinner_wallet, null);

			TopUpEmoneyProductResponseModel.Data.Denomination item = data.get(position);

			LinearLayout menuLayout = view.findViewById(R.id.menu_layout);
			TextView tvMenu = view.findViewById(R.id.tv_menu);

			tvMenu.setText(item.getProduct_name());

			return view;
		}
	}
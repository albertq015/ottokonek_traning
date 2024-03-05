package com.otto.mart.ui.Partials.adapter.transfer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;

import java.util.List;

public class BankSpinnerAdapter extends BaseAdapter {
		Context context;
		List<BankAccountModel> data;
		LayoutInflater inflter;

		public BankSpinnerAdapter(Context context, List<BankAccountModel> data) {
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
			view = inflter.inflate(R.layout.item_spinner_bank, null);

			BankAccountModel item = data.get(position);

			LinearLayout menuLayout = view.findViewById(R.id.menu_layout);
			TextView tvMenu = view.findViewById(R.id.tv_menu);
			TextView tvDesc = view.findViewById(R.id.tv_desc);

			tvMenu.setText(item.getBankName() + " - " + item.getAccountNumber());
			tvDesc.setText("a.n " + item.getAccountOwner());

			return view;
		}
	}
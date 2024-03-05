package com.otto.mart.ui.Partials.adapter.ppob.donasiOld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.donasi.ProductDonasiResponse;

import java.util.List;

public class DonasiProductAdapter extends BaseAdapter {
		Context context;
		List<ProductDonasiResponse.Data.Denomination> data;
		LayoutInflater inflter;

		public DonasiProductAdapter(Context context, List<ProductDonasiResponse.Data.Denomination> data) {
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
			view = inflter.inflate(R.layout.item_spinner_donasi_product, null);

			ProductDonasiResponse.Data.Denomination item = data.get(position);

			LinearLayout menuLayout = view.findViewById(R.id.menu_layout);
			TextView tvMenu = view.findViewById(R.id.tv_menu);
			ImageView imgLogo = view.findViewById(R.id.img_logo);

			if(position > 0){
				imgLogo.setVisibility(View.GONE);
			}

			Glide.with(context)
					.load(item.getImages())
					.apply(new RequestOptions().placeholder(R.drawable.border_white)
							.error(R.drawable.border_white))
					.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
					.into(imgLogo);

			tvMenu.setText(item.getOperator());

			return view;
		}
	}
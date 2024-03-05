package com.otto.mart.ui.fragment.olshop;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.SortModel;
import com.otto.mart.ui.Partials.adapter.olshop.SortAdapter;

import java.util.Objects;

public class SortFragment extends BottomSheetDialogFragment {

    RecyclerView recyclerView;
    private BottomSheetBehavior<FrameLayout> behavior;
    private SortModel sortModel;

    public static SortFragment newInstance() {

        Bundle args = new Bundle();

        SortFragment fragment = new SortFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        recyclerView = view.findViewById(R.id.sortList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SortAdapter(sortModel == null ? 0 : sortModel.getPos(), (id, pos, data) -> {
            try {
                ((AllCatalogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("all")).setSort((SortModel) data);
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        Objects.requireNonNull(bottomSheet).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void setSelectedSort(SortModel sortModel) {
        this.sortModel = sortModel;
    }
}

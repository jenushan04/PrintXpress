package com.printxpress.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.R;
import com.printxpress.app.model.Promotion;

import java.util.List;
import java.util.Locale;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.VH> {

    public interface OnClick { void onClick(Promotion p); }

    private final List<Promotion> items;
    private final OnClick listener;
    private final boolean adminMode;

    public PromotionAdapter(List<Promotion> items, OnClick listener, boolean adminMode) {
        this.items = items;
        this.listener = listener;
        this.adminMode = adminMode;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_promotion, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Promotion p = items.get(position);
        h.tvTitle.setText(p.getTitle());
        h.tvDescription.setText(p.getDescription());
        h.tvDiscount.setText(String.format(Locale.US, "%d%% OFF", p.getDiscountPercent()));
        h.tvValidity.setText("Valid until: " + (p.getValidUntil() == null ? "—" : p.getValidUntil()));

        if (adminMode && !p.isActive()) {
            h.tvInactive.setVisibility(View.VISIBLE);
        } else {
            h.tvInactive.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(p);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDiscount, tvValidity, tvInactive;
        VH(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvDescription = v.findViewById(R.id.tvDescription);
            tvDiscount = v.findViewById(R.id.tvDiscount);
            tvValidity = v.findViewById(R.id.tvValidity);
            tvInactive = v.findViewById(R.id.tvInactive);
        }
    }
}

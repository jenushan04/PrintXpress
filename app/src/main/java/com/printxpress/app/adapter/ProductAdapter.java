package com.printxpress.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.R;
import com.printxpress.app.model.Product;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    public interface OnProductClick {
        void onClick(Product product);
    }

    private final List<Product> items;
    private final OnProductClick listener;
    private final boolean showInactive;

    public ProductAdapter(List<Product> items, OnProductClick listener) {
        this(items, listener, false);
    }

    public ProductAdapter(List<Product> items, OnProductClick listener, boolean showInactive) {
        this.items = items;
        this.listener = listener;
        this.showInactive = showInactive;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Product p = items.get(position);
        h.tvName.setText(p.getName());
        h.tvCategory.setText(p.getCategory());
        h.tvPrice.setText(String.format(Locale.US, "LKR %.2f", p.getPrice()));
        h.tvSize.setText(p.getSizeOption() == null ? "" : p.getSizeOption());

        if (showInactive && !p.isActive()) {
            h.tvName.setAlpha(0.5f);
            h.tvInactiveBadge.setVisibility(View.VISIBLE);
        } else {
            h.tvName.setAlpha(1f);
            h.tvInactiveBadge.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(p);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice, tvSize, tvInactiveBadge;
        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvSize = v.findViewById(R.id.tvSize);
            tvInactiveBadge = v.findViewById(R.id.tvInactiveBadge);
        }
    }
}

package com.printxpress.app.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.R;
import com.printxpress.app.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.VH> {

    public interface OnOrderAction {
        void onClick(Order order);
    }

    private final List<Order> items;
    private final OnOrderAction listener;
    private final boolean adminMode;
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US);

    public OrderAdapter(List<Order> items, OnOrderAction listener, boolean adminMode) {
        this.items = items;
        this.listener = listener;
        this.adminMode = adminMode;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_order, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Order o = items.get(position);
        h.tvOrderId.setText(String.format(Locale.US, "Order #%d", o.getId()));
        h.tvProduct.setText(o.getProductName() == null ? "(deleted product)" : o.getProductName());
        h.tvQuantity.setText(String.format(Locale.US, "Qty: %d", o.getQuantity()));
        h.tvAmount.setText(String.format(Locale.US, "LKR %.2f", o.getTotalAmount()));
        h.tvDate.setText(dateFmt.format(new Date(o.getCreatedAt())));
        h.tvDelivery.setText(Order.DELIVERY_HOME.equals(o.getDeliveryType())
            ? "Home Delivery" : "Store Pickup");

        h.tvStatus.setText(o.getStatus());
        int color = statusColor(o.getStatus());
        GradientDrawable bg = (GradientDrawable) h.tvStatus.getBackground().mutate();
        bg.setColor(ContextCompat.getColor(h.itemView.getContext(), color));

        if (adminMode) {
            h.tvCustomer.setVisibility(View.VISIBLE);
            h.tvCustomer.setText("Customer: " +
                (o.getCustomerName() == null ? "—" : o.getCustomerName()));
        } else {
            h.tvCustomer.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(o);
        });
    }

    private int statusColor(String status) {
        if (status == null) return R.color.text_secondary;
        switch (status) {
            case Order.STATUS_PENDING: return R.color.status_pending;
            case Order.STATUS_PROCESSING: return R.color.status_processing;
            case Order.STATUS_PRINTING: return R.color.status_printing;
            case Order.STATUS_READY: return R.color.status_ready;
            case Order.STATUS_COMPLETED: return R.color.status_completed;
            case Order.STATUS_CANCELLED: return R.color.status_cancelled;
            default: return R.color.text_secondary;
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvProduct, tvQuantity, tvAmount, tvDate, tvDelivery, tvStatus, tvCustomer;
        VH(View v) {
            super(v);
            tvOrderId = v.findViewById(R.id.tvOrderId);
            tvProduct = v.findViewById(R.id.tvProduct);
            tvQuantity = v.findViewById(R.id.tvQuantity);
            tvAmount = v.findViewById(R.id.tvAmount);
            tvDate = v.findViewById(R.id.tvDate);
            tvDelivery = v.findViewById(R.id.tvDelivery);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvCustomer = v.findViewById(R.id.tvCustomer);
        }
    }
}

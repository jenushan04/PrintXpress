package com.printxpress.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.R;
import com.printxpress.app.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.VH> {

    private final List<User> items;
    private final SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy", Locale.US);

    public CustomerAdapter(List<User> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_customer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        User u = items.get(position);
        h.tvName.setText(u.getName());
        h.tvEmail.setText(u.getEmail());
        h.tvPhone.setText(u.getPhone());
        h.tvAddress.setText(u.getAddress() == null ? "" : u.getAddress());
        h.tvJoined.setText("Joined " + fmt.format(new Date(u.getCreatedAt())));
        String initial = u.getName() == null || u.getName().isEmpty()
            ? "?" : u.getName().substring(0, 1).toUpperCase(Locale.US);
        h.tvInitial.setText(initial);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone, tvAddress, tvJoined, tvInitial;
        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvEmail = v.findViewById(R.id.tvEmail);
            tvPhone = v.findViewById(R.id.tvPhone);
            tvAddress = v.findViewById(R.id.tvAddress);
            tvJoined = v.findViewById(R.id.tvJoined);
            tvInitial = v.findViewById(R.id.tvInitial);
        }
    }
}

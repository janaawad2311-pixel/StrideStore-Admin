
package com.example.stridestore_admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder> {

    // The fragment implements this, so the adapter can tell it about button clicks
    public interface OnShoeActionListener {
        void onUpdateClick(StrideStore shoe);

        void onDeleteClick(StrideStore shoe);
    }

    private final ArrayList<StrideStore> shoes;
    private final OnShoeActionListener listener;

    public ShoeAdapter(ArrayList<StrideStore> shoes,
                       OnShoeActionListener listener) {
        this.shoes = shoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shoe, parent, false);

        return new ShoeViewHolder(row);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ShoeViewHolder holder, int position) {

        StrideStore shoe = shoes.get(position);

        holder.tvName.setText(shoe.getName());
        holder.tvBrand.setText(shoe.getBrand());
        holder.tvPrice.setText(String.valueOf(shoe.getPrice()));

        // Glide downloads the shoe image from its URL and shows it
        Glide.with(holder.itemView.getContext())
                .load(shoe.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivLogo);

        holder.btnUpdate.setOnClickListener(v ->
                listener.onUpdateClick(shoe));

        holder.btnDelete.setOnClickListener(v ->
                listener.onDeleteClick(shoe));
    }

    @Override
    public int getItemCount() {
        return shoes.size();
    }

    public static class ShoeViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvBrand, tvPrice;
        ImageView ivLogo;
        Button btnUpdate, btnDelete;

        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            ivLogo = itemView.findViewById(R.id.ivLogo);

            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

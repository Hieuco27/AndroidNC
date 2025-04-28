package com.example.navigationfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.action.CSVCActivity;
import com.example.navigationfragment.databinding.ItemAddTaisanBinding;
import com.example.navigationfragment.databinding.ItemCsvcBinding;
import com.example.navigationfragment.entity.Asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class CSVCAdapter extends  RecyclerView.Adapter<CSVCAdapter.TaiSanViewHolder>{
    private List<Asset> assetList;
    private Context context;
    public CSVCAdapter(Context context, List<Asset> assetList) {
        this.context = context;
        this.assetList = assetList != null ? assetList : new ArrayList<>(); // Khởi tạo assetList nếu nó null
        notifyDataSetChanged();
    }


    private BiConsumer<Asset, Boolean> onAssetSelected;

    public CSVCAdapter(Context context, List<Asset> assetList, BiConsumer<Asset, Boolean> onAssetSelected) {
        this.context = context;
        this.assetList = assetList;
        this.onAssetSelected = onAssetSelected;
    }
    @NonNull
    @Override
    public TaiSanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCsvcBinding binding = ItemCsvcBinding.inflate(LayoutInflater.from(context),parent,false);
        TaiSanViewHolder viewHolder = new TaiSanViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaiSanViewHolder holder, int position) {
        Asset asset = assetList.get(position);
        if(asset == null){
            return;
        }
        holder.binding.tvAssetFee.setText(asset.getCompensationfee()+"");
        holder.binding.tvAssetName.setText(asset.getName());
        holder.itemView.setOnClickListener(v -> {
            boolean isSelected = false;
            onAssetSelected.accept(asset, isSelected);

        });

    }
    @Override
    public int getItemCount() {
        return  assetList.size();
    }


    public class TaiSanViewHolder extends RecyclerView.ViewHolder {

        ItemCsvcBinding binding;
        public TaiSanViewHolder(ItemCsvcBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

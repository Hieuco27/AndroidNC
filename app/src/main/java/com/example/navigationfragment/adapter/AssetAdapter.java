package com.example.navigationfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.databinding.ItemAddTaisanBinding;
import com.example.navigationfragment.entity.Asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetAdapter extends  RecyclerView.Adapter<AssetAdapter.TaiSanViewHolder>{
    private List<Asset> assetList;
    private Context context;
    private Map<String,Boolean> selectedAssets = new HashMap<>();
    public AssetAdapter(Context context, List<Asset> assetList) {
        this.context = context;
        this.assetList = assetList != null ? assetList : new ArrayList<>(); // Khởi tạo assetList nếu nó null
    }


    @NonNull
    @Override
    public TaiSanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddTaisanBinding binding = ItemAddTaisanBinding.inflate(LayoutInflater.from(context),parent,false);
        TaiSanViewHolder viewHolder = new TaiSanViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaiSanViewHolder holder, int position) {
        Asset asset = assetList.get(position);
        if(asset == null){
            return;
        }
        holder.binding.cbTaiSan.setText(asset.getName());
        holder.binding.cbTaiSan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedAssets.put(asset.getId(), true);
            } else {
                selectedAssets.remove(asset);
            }
        });

    }
    @Override
    public int getItemCount() {
        return  assetList.size();
    }

    public Map<String, Boolean> getSelectedAssets() {
        return selectedAssets;
    }

    // Thêm tài sản vào danh sách
    public void addTaiSan(Asset asset) {
        assetList.add(asset);
        notifyItemInserted(assetList.size() - 1);
    }

    public class TaiSanViewHolder extends RecyclerView.ViewHolder {

        ItemAddTaisanBinding binding;
        public TaiSanViewHolder(ItemAddTaisanBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

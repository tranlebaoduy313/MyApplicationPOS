package com.example.myapplicationpos;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpos.Models.MenuList;
import com.example.myapplicationpos.Models.ProductCategories;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Định nghĩa 2 loại item
    private static final int TYPE_HEADER = 0;  // Danh mục
    private static final int TYPE_ITEM = 1;    // Món ăn

    // List dữ liệu tổng hợp (Header + Items)
    private List<Object> dataList = new ArrayList<>();

    // Hàm để thêm dữ liệu (sẽ gọi từ Activity)
    public void setData(List<ProductCategories> categories, List<MenuList> menus) {
        dataList.clear();

        // Gom nhóm menus theo categoryId
        for (ProductCategories category : categories) {
            // Thêm header danh mục
            dataList.add(category);

            // Tìm các món ăn thuộc danh mục này
            for (MenuList menu : menus) {
                if (menu.getCategoryId().equals(category.getCategoryId())) {
                    dataList.add(menu);
                }
            }
        }

        notifyDataSetChanged();  // Cập nhật UI
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof ProductCategories) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        // Quyết định màu nền xen kẽ
//        int backgroundColor;
//        if (position % 2 == 0) {
//            backgroundColor = 0xFFFFFFFF;          // Trắng (#FFFFFF)
//        } else {
//            backgroundColor = 0xFFF5F5F5;          // Xám rất nhạt (#F5F5F5) - dễ nhìn
//            // Hoặc dùng: 0xFFE0E0E0  (xám nhạt hơn một chút)
//        }
//
//        // Áp dụng màu cho toàn bộ item
//        holder.itemView.setBackgroundColor(backgroundColor);
//
//        if (getItemViewType(position) == TYPE_HEADER) {
//            ProductCategories category = (ProductCategories) dataList.get(position);
//            ((HeaderViewHolder) holder).tvCategoryName.setText(category.getName());
//        } else {
//            MenuList menu = (MenuList) dataList.get(position);
//            ((ItemViewHolder) holder).tvMenuName.setText(menu.getName());
//            ((ItemViewHolder) holder).tvMenuPrice.setText(String.format("%.0f VND", menu.getPrice()));
//        }

        if (getItemViewType(position) == TYPE_HEADER) {
            ProductCategories category = (ProductCategories) dataList.get(position);
            ((HeaderViewHolder) holder).tvCategoryName.setText(category.getName());

            // Header KHÔNG dùng logic xen kẽ màu → đã được set từ XML drawable

        } else {
//            // Chỉ áp dụng xen kẽ màu cho món ăn
//            MenuList menu = (MenuList) dataList.get(position);
//            ItemViewHolder itemHolder = (ItemViewHolder) holder;
//            ((ItemViewHolder) holder).tvMenuName.setText(menu.getName());
//            ((ItemViewHolder) holder).tvMenuPrice.setText(String.format("%.0f VND", menu.getPrice()));
//
//            // Xen kẽ màu chỉ cho item món ăn
//            int backgroundColor;
//            if (position % 2 == 0) {
//                backgroundColor = 0xFFFFFFFF;      // Trắng
//            } else {
//                backgroundColor = 0xFFF5F5F5;      // Xám rất nhạt
//                // Hoặc: 0xFFE8ECEF (xám nhạt đẹp hơn)
//            }
//            holder.itemView.setBackgroundColor(backgroundColor);
            MenuList menu = (MenuList) dataList.get(position);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            itemHolder.tvMenuName.setText(menu.getName());
            itemHolder.tvMenuPrice.setText(String.format("%.0f VND", menu.getPrice()));

            // Load ảnh với Glide
            String imageUrl = menu.getImageUrl();

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemHolder.itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.no_image_food)  // Ảnh chờ load (tùy chọn)
                        .error(R.drawable.no_image_food)        // Ảnh lỗi hoặc null
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(itemHolder.ivMenuImage);
            } else {
                // Nếu null hoặc rỗng → dùng ảnh mặc định
                itemHolder.ivMenuImage.setImageResource(R.drawable.no_image_food);
            }

            // Xen kẽ màu nền cho món ăn (giữ nguyên code cũ của bạn)
            int backgroundColor;
            if (position % 2 == 0) {
                backgroundColor = 0xFFFFFFFF; // Trắng
            } else {
                backgroundColor = 0xFFF5F5F5; // Xám nhạt
            }
            holder.itemView.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // ViewHolder cho header
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    // ViewHolder cho item món ăn
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuName;
        TextView tvMenuPrice;
        ImageView ivMenuImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.tvMenuName);
            tvMenuPrice = itemView.findViewById(R.id.tvMenuPrice);
            ivMenuImage = itemView.findViewById(R.id.ivMenuImage);
        }
    }
}

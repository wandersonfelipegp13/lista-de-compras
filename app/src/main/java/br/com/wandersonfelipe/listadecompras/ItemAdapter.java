package br.com.wandersonfelipe.listadecompras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> items;
    private final Context context;
    private final ItemOnClickListener onClickListener;

    public interface ItemOnClickListener {
        void onClickItem(ItemViewHolder holder, int idx);
    }

    public ItemAdapter(Context context, ItemOnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.items = new ArrayList<>();
    }

    public int insertItem(Item item) {
        int pos = Collections.binarySearch(items, item, new ItemComparator());
        if (pos < 0) {
            pos = -pos - 1;
        }
        items.add(pos, item);
        notifyItemInserted(pos);
        return pos;
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void selectAll() {
        items.forEach(Item::select);
        notifyItemRangeChanged(0, items.size());
    }

    public void deselectAll() {
        items.forEach(Item::deselect);
        notifyItemRangeChanged(0, items.size());
    }

    public List<Item> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Item item = items.get(position);
        holder.textView.setText(item.getName());
        holder.textView.setTextSize(FontSizeUtils.getFontSize(context));
        holder.checkBox.setChecked(item.isSelected());

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(view -> onClickListener.onClickItem(holder, position));
            holder.checkBox.setOnClickListener(view -> onClickListener.onClickItem(holder, position));
        }

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public MaterialCheckBox checkBox;
        public TextView textView;
        public View view;

        public ItemViewHolder(View view) {
            super(view);
            this.view = view;
            this.checkBox = view.findViewById(R.id.checkbox);
            this.textView = view.findViewById(R.id.textView);
        }

    }

}

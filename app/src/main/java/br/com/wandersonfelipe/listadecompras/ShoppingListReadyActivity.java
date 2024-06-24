package br.com.wandersonfelipe.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListReadyActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_ready);

        toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShoppingListReadyActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ItemAdapter(ShoppingListReadyActivity.this, onClickItem());
        adapter.getItems().addAll(getItemsToBuy());
        recyclerView.setAdapter(adapter);

        updateTitle();
    }

    private List<Item> getItemsToBuy() {
        Intent intent = getIntent();
        String textList = intent.getStringExtra("items");

        if (textList == null || textList.isEmpty()) {
            Toast.makeText(getBaseContext(), getText(R.string.no_item_selected), Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }

        String[] itemsArray = textList.split("\n");

        List<Item> list = new ArrayList<>();

        for (String s : itemsArray) {
            s = s.replace("* ", "");
            list.add(new Item(s, true));
        }

        return list;
    }

    protected ItemAdapter.ItemOnClickListener onClickItem() {
        return ((holder, idx) -> {
            Item item = adapter.getItems().get(idx);
            item.toggleSelection();
            holder.checkBox.setChecked(item.isSelected());
            updateTitle();
        });
    }

    private void updateTitle() {
        long toBuySize = this.adapter.getItems().stream().filter(Item::isSelected).count();
        if (toBuySize == 0) toolbar.setTitle(R.string.no_items_missing);
        else if (toBuySize == 1) toolbar.setTitle(R.string.one_item_missing);
        else {
            toolbar.setTitle(getString(R.string.missing) + " " + toBuySize + " " + getString(R.string.items));
        }
    }

}
package br.com.wandersonfelipe.listadecompras;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ItemAdapter(MainActivity.this, onClickItem());
        ItemList.items().forEach(adapter::insertItem);
        recyclerView.setAdapter(adapter);

        toolbar = findViewById(R.id.toolbar);

        configureItemSwipe();
        configureMenu();

    }

    private void configureMenu() {
        toolbar.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.paste_list) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = clipboard.getPrimaryClip();

                String list;

                if (clipData == null || clipData.getItemAt(0) == null ||
                        clipData.getItemAt(0).getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(getBaseContext(),
                            getResources().getText(R.string.no_clipboard),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                list = clipData.getItemAt(0).getText().toString();
                Intent intent = new Intent(MainActivity.this, ShoppingListReadyActivity.class);
                intent.putExtra("items", list);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.done) {
                String items = getSelectedItems(false);
                if (items.isEmpty()) {
                    Toast.makeText(getBaseContext(),
                            getResources().getText(R.string.select_a_item),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent(MainActivity.this, ShoppingListReadyActivity.class);
                intent.putExtra("items", items);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.send) {
                String items = getSelectedItems(true);
                if (items.isEmpty()) {
                    Toast.makeText(getBaseContext(),
                            getResources().getText(R.string.select_a_item),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getSelectedItems(true));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            } else if (itemId == R.id.select_all) {
                adapter.selectAll();
            } else if (itemId == R.id.deselect_all) {
                adapter.deselectAll();
            } else if (itemId == R.id.add_item) {
                View inputLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.text_input_dialog, null);
                TextInputEditText editText = inputLayout.findViewById(R.id.editText);

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle(R.string.new_item)
                        .setView(inputLayout);

                builder.setPositiveButton(R.string.add, null);
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

                AlertDialog myDialog = builder.create();
                myDialog.show();

                myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(s.length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}

                });

                myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    if (editText.getText() == null) {
                        myDialog.cancel();
                    }

                    String itemName = editText.getText().toString();

                    Item item = new Item(itemName, false);

                    recyclerView.scrollToPosition(adapter.insertItem(item));

                    myDialog.dismiss();

                });

            } else if (itemId == R.id.small_font) {
                FontSizeUtils.setFontSize(MainActivity.this, FontSize.SMALL);
                refreshList();
            } else if (itemId == R.id.medium_font) {
                FontSizeUtils.setFontSize(MainActivity.this, FontSize.MEDIUM);
                refreshList();
            } else if (itemId == R.id.big_font) {
                FontSizeUtils.setFontSize(MainActivity.this, FontSize.BIG);
                refreshList();
            }
            return false;
        });
    }

    private void refreshList() {
        int pos = linearLayoutManager.findFirstVisibleItemPosition();
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(pos);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void configureItemSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.exclude) + " " + adapter.getItems().get(position).getName() + "?");

                builder.setPositiveButton(R.string.yes, (dialog, which) -> adapter.deleteItem(position));

                builder.setNegativeButton(R.string.no, (dialog, which) -> {
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                });

                builder.setOnCancelListener(dialog -> adapter.notifyDataSetChanged());

                recyclerView.scrollToPosition(position);

                builder.create().show();

            }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    protected ItemAdapter.ItemOnClickListener onClickItem() {
        return ((holder, idx) -> {
            Item item = adapter.getItems().get(idx);
            item.toggleSelection();
            holder.checkBox.setChecked(item.isSelected());
        });
    }

    private String getSelectedItems(boolean useBullet) {
        StringBuilder builder = new StringBuilder();

        this.adapter.getItems().forEach(item -> {
            if (item.isSelected()) {
                if (useBullet) {
                    builder.append("* ");
                }
                builder.append(item.getName()).append('\n');
            }
        });

        return builder.toString().trim();

    }

}
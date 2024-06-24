package br.com.wandersonfelipe.listadecompras;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class ItemComparator implements Comparator<Item> {

    private final Collator collator;

    public ItemComparator() {
        collator = Collator.getInstance(new Locale("pt", "BR"));
        collator.setStrength(Collator.PRIMARY);
    }

    @Override
    public int compare(Item item1, Item item2) {
        return collator.compare(item1.getName(), item2.getName());
    }

}

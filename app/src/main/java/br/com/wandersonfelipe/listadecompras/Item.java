package br.com.wandersonfelipe.listadecompras;

public class Item {

    private String name;
    private boolean selected;

    public Item() {
    }

    public Item(String name, boolean isSelected) {
        this.name = name;
        this.selected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public void toggleSelection() {
        selected = !selected;
    }

}

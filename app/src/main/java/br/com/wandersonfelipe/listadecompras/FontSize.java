package br.com.wandersonfelipe.listadecompras;

public enum FontSize {

    SMALL(24f), MEDIUM(32f), BIG(48f);

    private final float size;

    FontSize(float size) {
        this.size = size;
    }

    public float size() {
        return this.size;
    }

}

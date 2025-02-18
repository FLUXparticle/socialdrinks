package com.example.socialdrinks.fridge.model;

public class FridgeIngredient {

    private Long id;

    private String name;

    private boolean inFridge;

    // Konstruktoren
    protected FridgeIngredient() {
        // Für JPA
    }

    public FridgeIngredient(Long id, boolean inFridge) {
        this.id = id;
        this.inFridge = inFridge;
    }

    public FridgeIngredient(Long id, String name, boolean inFridge) {
        this(id, inFridge);
        this.name = name;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long ingredientId) {
        this.id = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInFridge() {
        return inFridge;
    }

    public void setInFridge(boolean inFridge) {
        this.inFridge = inFridge;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FridgeIngredient{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", inFridge=").append(inFridge);
        sb.append('}');
        return sb.toString();
    }

}

package com.example.socialdrinks.model.entity;

import jakarta.persistence.*;

@Entity
public class Instruction {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Integer amountCL;

    @ManyToOne
    private Ingredient ingredient;

    protected Instruction() {
        // Required by JAXB
    }

    public Instruction(Integer amountCL, Ingredient ingredient) {
        this.amountCL = amountCL;
        this.ingredient = ingredient;
    }

    public Integer getAmountCL() {
        return amountCL;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (amountCL != null) {
            sb.append(amountCL);
            sb.append("cl ");
        }
        sb.append(ingredient.getName());

        return sb.toString();
    }

}

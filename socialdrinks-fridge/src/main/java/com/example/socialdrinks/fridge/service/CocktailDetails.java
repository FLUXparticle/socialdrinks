package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.model.entity.*;

import java.util.*;

public class CocktailDetails {

    private String name;

    private Collection<Instruction> instructions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(Collection<Instruction> instructions) {
        this.instructions = instructions;
    }

}

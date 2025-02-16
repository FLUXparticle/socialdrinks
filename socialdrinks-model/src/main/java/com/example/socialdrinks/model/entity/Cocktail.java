package com.example.socialdrinks.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.*;

import java.util.*;

@Entity
public class Cocktail implements Comparable<Cocktail> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn // (name="cocktail_id")
    @JsonIgnore
    private Collection<Instruction> instructions;

    protected Cocktail() {
        // Required by JAXB
    }

    public Cocktail(String name, List<Instruction> instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        String instructionsString = Hibernate.isInitialized(instructions) ? String.valueOf(instructions) : "[...]";
        return "Cocktail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", instructions=" + instructionsString +
                '}';
    }

    @Override
    public int compareTo(Cocktail other) {
        return name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cocktail cocktail = (Cocktail) o;
        return id.equals(cocktail.id) && name.equals(cocktail.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Instruction> getInstructions() {
        return instructions;
    }

}

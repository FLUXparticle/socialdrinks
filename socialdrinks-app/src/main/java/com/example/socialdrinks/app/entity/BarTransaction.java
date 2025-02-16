package com.example.socialdrinks.app.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "bar_transaction")
public class BarTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bar_id", insertable = true, updatable = false)
    private Bar bar;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private String action;

    @Column(length = 1000)
    private String details;

    // Standard-Konstruktor
    public BarTransaction() {
    }

    public BarTransaction(Bar bar, String action, String details) {
        this.bar = bar;
        this.action = action;
        this.details = details;
        this.timestamp = new Date();
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}

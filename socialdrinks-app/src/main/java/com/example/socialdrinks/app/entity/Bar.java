package com.example.socialdrinks.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.*;

@Entity
public class Bar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name ist erforderlich.")
    @Size(max = 20, message = "Maximal 20 Zeichen")
    private String name;

    @NotBlank(message = "E-Mail ist erforderlich.")
    @Email(message = "Gültige E-Mail-Adresse")
    private String email;

    @Size(max = 20, message = "Maximal 20 Zeichen")
    private String address;

    @Version
    private Long version;

    // Standard-Konstruktor
    public Bar() {
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Bar{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", version=" + version +
                '}';
    }

}

package com.example.demo.model;

import javax.validation.constraints.NotBlank; // Importar a anotação

public class Item {
    private Long id;

    @NotBlank(message = "Name is mandatory") // Não pode ser nulo ou conter apenas espaços em branco
    private String name;

    @NotBlank(message = "Description is mandatory") // Mensagem de erro customizada
    private String description;

    public Item() {
    }

    public Item(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters e Setters permanecem os mesmos...
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
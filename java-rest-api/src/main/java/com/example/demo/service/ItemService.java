package com.example.demo.service;

import com.example.demo.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ItemService {

    private List<Item> items = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();
    private Long currentId = 1L;


    public List<Item> getAllItems() {
        return items;
    }

    public Optional<Item> getItemById(Long id) {
        return items.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();
    }

    public Item createItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }

        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia");
        }

        // ✅ AQUI ESTÁ A CORREÇÃO PRINCIPAL
        item.setId(currentId++);

        items.add(item);
        return item;
    }

    //NOVOS
    public Optional<Item> updateItem(Long id, Item newItemData) {
        return getItemById(id).map(existingItem -> {
            existingItem.setName(newItemData.getName());
            existingItem.setDescription(newItemData.getDescription());
            return existingItem;
        });
    }

    public boolean deleteItem(Long id) {
        if (getItemById(id).isPresent()) {
            items.removeIf(item -> id.equals(item.getId()));
            return true;
        }
        return false;
    }

    public Item findById(Long id) {
        if (id == null) return null;

        return items.stream()
                .filter(item -> item.getId() != null && item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Item> searchByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        return items.stream()
                .filter(item -> item.getName() != null &&
                        item.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public Item updateStatus(Long id, boolean active) {

        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        Item item = findById(id);

        if (item == null) {
            throw new RuntimeException("Item não encontrado");
        }

        item.setActive(active);
        return item;
    }


}
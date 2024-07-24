package dev.quasemago.aura.client.commands;

import lombok.Getter;

@Getter
public enum CommandCategory {
    GENERAL("General"),
    SEARCHES("Searches");

    private final String name;

    CommandCategory(String name) {
        this.name = name;
    }
}

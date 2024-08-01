package dev.quasemago.aura.client.interfaces.commands;

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

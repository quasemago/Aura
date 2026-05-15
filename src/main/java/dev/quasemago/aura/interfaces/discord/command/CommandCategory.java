package dev.quasemago.aura.interfaces.discord.command;

public enum CommandCategory {
    NONE("CMD_CATEGORY_NONE"),
    GENERAL("CMD_CATEGORY_GENERAL"),
    SEARCHES("CMD_CATEGORY_SEARCHES");

    private final String translationKey;

    CommandCategory(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }
}

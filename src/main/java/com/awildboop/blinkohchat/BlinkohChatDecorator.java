package com.awildboop.blinkohchat;

public enum BlinkohChatDecorator {
    Prefix("prefix"),
    Suffix("suffix"),
    Color("color"),
    Colour("color"), // because i like to use colour a lot

    Personal("personal"),
    Global("global"); // including these here because it doesn't make sense to make another enum just for them


    private final String label;
    private BlinkohChatDecorator(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

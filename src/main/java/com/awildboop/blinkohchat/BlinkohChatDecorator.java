package com.awildboop.blinkohchat;

public enum BlinkohChatDecorator {
    Prefix("prefix"),
    Suffix("suffix"),
    Color("color"),
    Colour("color"); // because I like to use colour a lot


    private final String label;
    BlinkohChatDecorator(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

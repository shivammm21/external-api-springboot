package com.example.externalapispringboot.request;

import lombok.Data;
import java.util.List;

@Data
public class Content {
    private List<ContentItem> contents;

    @Data
    public static class ContentItem {
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }

    @Override
    public String toString() {
        if (contents != null && !contents.isEmpty() && 
            contents.get(0).getParts() != null && !contents.get(0).getParts().isEmpty()) {
            return contents.get(0).getParts().get(0).getText();
        }
        return "";
    }
}

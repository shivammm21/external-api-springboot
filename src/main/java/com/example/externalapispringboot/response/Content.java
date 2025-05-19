package com.example.externalapispringboot.response;

import lombok.Data;

import java.util.List;

@Data
public class Content {
    private List<Part> parts;
    private String role;

} 
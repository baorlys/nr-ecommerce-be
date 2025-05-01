package com.nr.ecommercebe.shared.event;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDeleteEvent {
    private String imageUrl;
}

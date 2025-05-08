package com.nr.ecommercebe.module.media.event;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ImageDeleteEvent implements Serializable {
    private String imageUrl;
}

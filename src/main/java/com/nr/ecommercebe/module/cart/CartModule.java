package com.nr.ecommercebe.module.cart;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        allowedDependencies = {"product", "media", "order", "user"}
)
public class CartModule {
}

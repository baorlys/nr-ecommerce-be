package com.nr.ecommercebe.module.catalog;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        allowedDependencies = {"user", "media"}
)
public class CatalogModule {
}

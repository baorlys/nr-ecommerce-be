package com.nr.ecommercebe.module.review;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(allowedDependencies = {"catalog", "user"})
public class ReviewModule {
}

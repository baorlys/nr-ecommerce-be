package com.nr.ecommercebe.shared.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// E - Entity type
// I - Identifier type
// Q - Request DTO type
// S - Response DTO type
public interface BaseCrudService<I, Q, S> {
    S create(Q request);
    S update(I id, Q request);
    void delete(I id);
    S getById(I id);
    Page<S> getAll(Pageable pageable);
}
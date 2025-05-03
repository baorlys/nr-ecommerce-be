package com.nr.ecommercebe.web.admin;

import com.nr.ecommercebe.module.user.api.UserService;
import com.nr.ecommercebe.module.user.api.response.UserResponseDto;
import com.nr.ecommercebe.shared.model.PagedResponseSuccess;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserAdminController {
    UserService userService;

    @GetMapping
    public ResponseEntity<PagedResponseSuccess<UserResponseDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UserResponseDto> userPage = userService.getAll(pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Users fetched successfully", userPage));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }



}

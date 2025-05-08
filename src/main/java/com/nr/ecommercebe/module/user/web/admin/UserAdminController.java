package com.nr.ecommercebe.module.user.web.admin;

import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import com.nr.ecommercebe.module.user.application.service.manager.UserService;
import com.nr.ecommercebe.shared.dto.PagedResponseSuccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin Users", description = "APIs for managing users in the admin panel")
public class UserAdminController {

    UserService userService;

    @GetMapping
    @Operation(
            summary = "Get users",
            description = "Fetch a paginated list of users with optional pagination parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users fetched successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
            }
    )
    public ResponseEntity<PagedResponseSuccess<UserResponseDto>> getUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of users per page") @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UserResponseDto> userPage = userService.getAll(pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Users fetched successfully", userPage));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Deletes a user from the system by their ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID to delete") @PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

package com.ukma.cleaning.user;

import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPageDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Endpoint for operations with users (customers/staff)")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by id", description = "Get user by id")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Get user by email", description = "Get user by email")
    @GetMapping("/by-email/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }

    @Operation(summary = "Change user", description = "Change user")
    @PutMapping
    public UserDto updateUser(@RequestBody @Valid UserDto userDto) {
        return userService.update(userDto);
    }

    @Operation(summary = "Change user", description = "Change user")
    @PutMapping("/pass")
    public UserDto updatePassword(@RequestBody @Valid UserPasswordDto userDto) {
        return userService.updatePassword(userDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add user", description = "Add user")
    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserRegistrationDto userDto) {
        return userService.create(userDto);
    }

    @Operation(summary = "Delete user", description = "Delete user")
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @GetMapping("/findAllByRole")
    public UserPageDto findAllByRole(@RequestParam(defaultValue = "USER") Role role, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return userService.findUsersByPageAndRole(role, pageable);
    }
}

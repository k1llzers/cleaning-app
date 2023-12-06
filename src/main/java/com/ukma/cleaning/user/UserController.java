package com.ukma.cleaning.user;

import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Endpoint for operations with users (customers/staff)")
public class UserController {
    private final UserService userService;
//    private final AuthenticationService authenticationService;

    @Operation(summary = "Get user by id", description = "Get user by id")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Get user by email", description = "Get user by email")
    @GetMapping("/by-email/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        RestTemplate restTemplate = new RestTemplate();
        String resource = "http://worldtimeapi.org/api/timezone/Europe/Kyiv";
        ResponseEntity<String> time = restTemplate.getForEntity(resource, String.class);
        log.info("time from api:" + time.getBody());
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }

//    @ExceptionHandler({EmailDuplicateException.class})
//    public void handleException(Exception ex) {
//        log.warn("Exception occurred");
//    }
}

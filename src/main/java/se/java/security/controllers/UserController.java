package se.java.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.java.security.dto.UserUpdateRequest;
import se.java.security.models.User;
import se.java.security.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/update")
    public ResponseEntity<User> updateUserInfo(@RequestBody UserUpdateRequest updatedUser) {
        User user = userService.updateUserInfo(updatedUser);
        return ResponseEntity.ok(user);
    }

}

package com.app.ecom.controller;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.service.UserService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;




import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;




@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponse>>getALLUsers(){
        return ResponseEntity.ok(userService.fetchAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest updatedUserRequest) {

        boolean updated = userService.updateUser(id, updatedUserRequest);

        if (updated) {
            return ResponseEntity.ok("User updated successfully");
        }

        return ResponseEntity.notFound().build();
    

    }
    @PostMapping
    public ResponseEntity<String> addUser(
            @RequestBody UserRequest userRequest) {

        userService.addUser(userRequest);

        return ResponseEntity.ok("User created successfully");
    }

}

    



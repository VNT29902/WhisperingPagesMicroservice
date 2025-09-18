package com.example.UserService.Controller;

import com.example.UserService.DTO.ShippingAddressRequest;
import com.example.UserService.DTO.ShippingAddressResponse;
import com.example.UserService.DTO.UserCreateRequest;
import com.example.UserService.DTO.UserUpdateRequest;
import com.example.UserService.Response.UserResponse;
import com.example.UserService.Service.ShippingAddressService;
import com.example.UserService.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ShippingAddressService shippingService;
    public UserController(UserService userService, ShippingAddressService shippingService) {
        this.userService = userService;
        this.shippingService = shippingService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest req) {
        return ResponseEntity.ok(userService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getByUserName(@RequestHeader("X-User-Name") String userName) {
        return ResponseEntity.ok(userService.getByUserName(userName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id,
                                               @Valid @RequestBody UserUpdateRequest req) {
        return ResponseEntity.ok(userService.update(id, req));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // shipping

    @GetMapping("/shipping")
    public ResponseEntity<List<ShippingAddressResponse>> getByShippingByUser(@RequestHeader("X-User-Name") String userName) {
        return ResponseEntity.ok(shippingService.getByUserName(userName));
    }

    @PutMapping("/shipping/{id}")
    public ResponseEntity<ShippingAddressResponse> updateShippingByUserName(@RequestHeader("X-User-Name") String userName,
                                                            @PathVariable String id,
                                                            @RequestBody ShippingAddressRequest address) {
        return ResponseEntity.ok(shippingService.updateByUserName(id, userName, address));
    }

    @DeleteMapping("/shipping/{id}")
    public ResponseEntity<Void> deleteShippingById(@PathVariable String id) {
        shippingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/shipping")
    public ResponseEntity<ShippingAddressResponse> createShipping(@RequestHeader("X-User-Name") String userName,
                                                  @RequestBody ShippingAddressRequest address) {
        return ResponseEntity.ok(shippingService.create(userName, address));
    }


}

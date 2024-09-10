package com.logs.Sec.controller;

import com.logs.Sec.dto.ReqRes;
import com.logs.Sec.model.OurUsers;
import com.logs.Sec.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersManagementController {
    @Autowired
    private UsersManagementService usersManagementService;

    @PostMapping("/auth/register")
    ResponseEntity<ReqRes> register(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.register(req));
    }

    @PostMapping("/auth/login")
    ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-users/{userId}")
    ResponseEntity<ReqRes> getUserById(@PathVariable int userId){
        return ResponseEntity.ok(usersManagementService.getUserById(userId));
    }

    @PutMapping("/admin/update/{userId}")
    ResponseEntity<ReqRes> updateUser(@PathVariable int userId, @RequestBody OurUsers req){
        return ResponseEntity.ok(usersManagementService.updateUser(userId,req));
    }

    @DeleteMapping("/admin/delete/{userId}")
    ResponseEntity<ReqRes> deleteUser(@PathVariable int userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @GetMapping("/adminuser/get-profile")
    ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes resp = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}

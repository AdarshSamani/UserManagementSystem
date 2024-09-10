package com.logs.Sec.service;

import com.logs.Sec.dto.ReqRes;
import com.logs.Sec.model.OurUsers;
import com.logs.Sec.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {
    @Autowired
    private UserRepo repo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            OurUsers ourUsers = new OurUsers();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setCity(registrationRequest.getCity());
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setName(registrationRequest.getName());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers ourUserResult = repo.save(ourUsers);
            if(ourUserResult.getId() > 0){
                resp.setOurUsers(ourUserResult);
                resp.setMessage("User Save Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes login(ReqRes loginRequest){
        ReqRes resp = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            var user = repo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(),user);
            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRole(user.getRole());
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24Hrs");
            resp.setMessage("Successfully logged in");
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes resp = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers users = repo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getRefreshToken(),users)){
                var jwt = jwtUtils.generateToken(users);
                resp.setStatusCode(200);
                resp.setToken(jwt);
                resp.setRefreshToken(refreshTokenRequest.getRefreshToken());
                resp.setExpirationTime("24Hrs");
                resp.setMessage("Successfully Refreshed Token");
            }
            resp.setStatusCode(200);
            return resp;
        }catch (Exception e){
            resp.setStatusCode(500);
            return resp;
        }
    }

    public ReqRes getAllUsers(){
        ReqRes resp = new ReqRes();
        try{
            List<OurUsers> result = repo.findAll();
            if(!result.isEmpty()){
                resp.setOurUsersList(result);
                resp.setStatusCode(200);
                resp.setMessage("SuccessFul");
            }else {
                resp.setStatusCode(404);
                resp.setMessage("No users found");
            }
            return resp;
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: "+e.getMessage());
            return resp;
        }
    }

    public ReqRes getUserById(int id){
        ReqRes resp = new ReqRes();
        try {
            OurUsers userById = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            resp.setOurUsers(userById);
            resp.setStatusCode(200);
            resp.setMessage("User with id "+id+" found successfully");
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: "+e.getMessage());
        }
        return resp;
    }

    public ReqRes deleteUser(int userId){
        ReqRes resp = new ReqRes();
        try {
            Optional<OurUsers> userOptional = repo.findById(userId);
            if(userOptional.isPresent()){
                repo.deleteById(userId);
                resp.setStatusCode(200);
                resp.setMessage("User deleted successfully");
            }else {
                resp.setStatusCode(404);
                resp.setMessage("User not found successfully");
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage("Error occurred while deleting user: "+e.getMessage());
        }
        return resp;
    }

    public ReqRes updateUser(int userId,OurUsers updatedUser){
        ReqRes resp = new ReqRes();
        try {
            Optional<OurUsers> userOptional = repo.findById(userId);
            if(userOptional.isPresent()){
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());
                existingUser.setName(updatedUser.getName());
                if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()){
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                OurUsers saveduser = repo.save(existingUser);
                resp.setOurUsers(saveduser);
                resp.setStatusCode(200);
                resp.setMessage("User updated successfully");
            }else {
                resp.setStatusCode(404);
                resp.setMessage("User not found for update");
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage("Error occurred while updating user: "+e.getMessage());
        }
        return resp;
    }

    public ReqRes getMyInfo(String email) {
        ReqRes resp = new ReqRes();
        try {
            Optional<OurUsers> userOptional = repo.findByEmail(email);
            if(userOptional.isPresent()){
                resp.setOurUsers(userOptional.get());
                resp.setStatusCode(200);
                resp.setMessage("Successful");
            }else {
                resp.setStatusCode(404);
                resp.setMessage("User not found");
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage("Error occurred while getting user info: "+e.getMessage());
        }
        return resp;
    }
}

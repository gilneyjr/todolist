package com.gilneyjr.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody UserModel userModel) {
        UserModel user = this.userRepository.findByUsername(userModel.getUsername());
        
        if (user != null) {
            System.out.println("Usuário já existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("User already exist");   
        }

        var passwordHashed = BCrypt.withDefaults()
            .hashToString(12, userModel.getPassword()
            .toCharArray());

        userModel.setPassword(passwordHashed);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.userRepository.save(userModel));
    }
}

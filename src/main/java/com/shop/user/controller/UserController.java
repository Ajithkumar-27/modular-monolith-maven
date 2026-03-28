package com.shop.user.controller;
import java.util.List;import org.springframework.web.bind.annotation.*;import com.shop.user.model.User;import com.shop.user.service.UserService;
@RestController @RequestMapping("/users")
public class UserController {
 private final UserService service; public UserController(UserService service){this.service=service;}
 @PostMapping public User create(@RequestBody User user){return service.save(user);} 
 @GetMapping public List<User> all(){return service.findAll();}
}

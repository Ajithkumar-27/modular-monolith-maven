package com.shop.user.service;
import java.util.List;import org.springframework.stereotype.Service;import com.shop.user.model.User;import com.shop.user.repository.UserRepository;
@Service
public class UserService {
 private final UserRepository repo; public UserService(UserRepository repo){this.repo=repo;}
 public User save(User u){return repo.save(u);} public List<User> findAll(){return repo.findAll();}
}

package com.shop.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.shop.user.model.User;
import com.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    @SuppressWarnings("null")
    public User save(User u) {
        return repo.save(u);
    }

    public List<User> findAll() {
        return repo.findAll();
    }
}

package com.example.demo.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.Cart;
import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.News;
import com.example.demo.model.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CartRepository cartRepository;         
    
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public UserDto getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return userMapper.toDto(user);
    }

    @Override
    public void addUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
    }
    public UserDto login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {

            // ✅ 若該用戶沒有購物車，就新建一個並綁定
            if (cartRepository.findByUserId(user.getId()).isEmpty()) {
                Cart cart = new Cart();
                cart.setUser(user);
                cartRepository.save(cart);
            }

            return userMapper.toDto(user);
        }
        return null;
    }
    
    @Override
    public List<Cart> getUserCarts(Integer userId) {
        return cartRepository.findByUserId(userId); 
    }
    
    @Override
    public Set<Favorite> getUserFavorites(Integer userId) {
        return new HashSet<>(favoriteRepository.findByUserId(userId)); 
    }
}
   
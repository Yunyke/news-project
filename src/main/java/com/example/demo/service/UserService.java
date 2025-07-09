package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.News;
import com.example.demo.model.entity.Cart;
import com.example.demo.model.entity.Favorite;

public interface UserService {
	public UserDto getUser(String username);

	public void addUser(UserDto userDto);

	// 登入並回傳資料
	UserDto login(String username, String password);

	// 取得購物車
	List<Cart> getUserCarts(Integer userId);

	// 取得收藏清單
	Set<Favorite> getUserFavorites(Integer userId);
}
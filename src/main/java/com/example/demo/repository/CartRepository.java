package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Cart;

import jakarta.transaction.Transactional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	List<Cart> findByUserId(Integer userId);
	
	@Query("SELECT c.news.id FROM CartItem c WHERE c.cart.user.id = :userId")
	List<Long> findNewsIdsByUserId(Integer userId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem ci WHERE ci.cart.user.id = :userId AND ci.news.id = :newsId")
	void deleteByUserIdAndNewsId(Integer userId, Long newsId);
	 
	 Optional<Cart> findByIdAndUserId(Long cartId, Integer userId);
	 
}
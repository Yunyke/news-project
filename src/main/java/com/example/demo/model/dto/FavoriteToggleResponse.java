package com.example.demo.model.dto;
//record 的設計目的是為了響應使用者執行「收藏/取消收藏」新聞操作後，向前端提供操作結果的資訊。
public record FavoriteToggleResponse(Long newsId, boolean favorited) {
	

}
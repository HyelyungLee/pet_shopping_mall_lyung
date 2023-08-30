package com.shop.app.review.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewListDto {

	private int reviewId;
	private int orderId;
	private int productId;
	private int productDetailId;
	
	private int reviewStarRate;
	private LocalDateTime reviewCreatedAt;
	
	private String productName; // product
	private String optionName; // product_detail
	private String optionValue; // product_detail

	private String imageOriginalFileName;
	private String imageRenamedFileName;
	
}
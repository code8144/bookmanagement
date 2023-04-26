package com.toyproject.bookmanagement.dto.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetBookRespDto {		// 책을 눌렀을때 화면
	private int bookId;
	private String bookName;
	private String authorName;
	private String publisherName;
	private String categoryName;
	private String coverImgUrl;
}

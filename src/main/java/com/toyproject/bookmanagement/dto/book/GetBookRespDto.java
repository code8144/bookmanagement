package com.toyproject.bookmanagement.dto.book;

import lombok.Builder;
import lombok.Data;

//하나의 요청에는 하나의 디티오를 사용하는 것이 좋음 
@Builder
@Data
public class GetBookRespDto {
	private int bookId;
	private String bookName;
	private String authorName;
	private String publisherName;
	private String categoryName;
	private String coverImgUrl;

}

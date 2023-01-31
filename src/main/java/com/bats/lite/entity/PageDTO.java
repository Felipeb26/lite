package com.bats.lite.entity;

import com.bats.lite.mapper.UserMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@NoArgsConstructor
public class PageDTO<E, R> {

	@Autowired
	private UserMapper mapper;
	private long totalElements;
	private int totalPages;
	private int currentPage;
	private int pageSize;
	private long numberOfElements;
	private boolean hasNext;
	private List<?> content;

	public PageDTO(Page<E> page, Function<List<E>, List<R>> function) {
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.currentPage = page.getNumber();
		this.pageSize = page.getSize();
		this.numberOfElements = page.getNumberOfElements();
		this.hasNext = page.hasNext();
		this.content = function.apply(page.getContent());
	}

}
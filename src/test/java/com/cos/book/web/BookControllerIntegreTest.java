package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdk.internal.org.jline.utils.Log;

//통합 테스트(모든 Bean들을 똑같이 IoC 올리고 테스트 하는 것)
/**
 * WebEnvironment.MOCK = 실제 톰켓을 올리는게 아니라 다른 톰켓으로 테스트
 * WebEnvironment.RANDOM_POR = 실제 톰켓으로 테스트
 * @@AutoConfigureMockMvc : MockMvc를 Ioc에 등록해줌
 * @@Transactional: 각각의 테스트 함 수가 종료될 때마다 트랜잭션을 rollback해줌
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) //실제 톰켓을 올리는게 아니라 다른 톰켓으로 테스트
public class BookControllerIntegreTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void save_테스트() {
		Log.info("save_테스트()시작 ===================");
	}
	
	
	
	
	
	
	
	@Test
	public void findById_테스트() throws Exception{
		//given
		Long id = 2L;
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기", "코스"));
		books.add(new Book(null,"Junit 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("리액트 따라하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception{
		//given		
		Long id = 1L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기", "코스"));
		books.add(new Book(null,"Junit 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		Book book = new Book(null, "C++따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);
		
		//when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(3L))
		.andExpect(jsonPath("$.title").value("C++따라하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_테스트() throws Exception{
		//given		
		Long id = 1L;
		
		when(bookService.삭제하기(id)).thenReturn("ok");
		
		//when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.TEXT_PLAIN));
		
		//then
		resultAction
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = resultAction.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		
		assertEquals("ok", result);
	}

}

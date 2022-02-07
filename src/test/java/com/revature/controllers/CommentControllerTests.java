package com.revature.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dto.CommentDTO;
import com.revature.models.Comment;
import com.revature.models.Post;
import com.revature.models.Profile;
import com.revature.services.CommentServiceImpl;
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTests {

	private MockMvc mockMvc;
	private static String baseUrl = "/comment";
	   @Autowired
	    private WebApplicationContext webApplicationContext;
	   
	   @Autowired
	   private ObjectMapper objectMapper;

	   @MockBean
	   CommentServiceImpl cServ;
	  @BeforeEach
	    public void setup() {
	        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	    }
	@Test
	void testAddComments_thenReturns200() throws Exception {
	Profile profile = new Profile();
	Post post = new Post();
	Comment previous = new Comment();
	Timestamp tStamp = new Timestamp(40);
	Comment c = new Comment(1, profile, post, "body", tStamp, previous );
	
	mockMvc.perform(MockMvcRequestBuilders.post("/comment")
								.contentType("application/json")
									.param("comment", "true")
									 .content(objectMapper.writeValueAsString(c)))
									.andExpect(status().isCreated());
											
	
	}
	
	@Test
	void testGetComments_thenReturns() throws Exception {
		Profile profile = new Profile();
		Post post = new Post();
		Comment previous = new Comment();
		Timestamp tStamp = new Timestamp(40);
		Comment c = new Comment(1730099524, profile, post, "body", tStamp, previous );
		List<Comment> cList = new ArrayList();
		cList.add(c);
		when(cServ.getCommentsByPostPsid(1730099524)).thenReturn(cList);
		List<Comment> cListTest = cServ.getCommentsByPostPsid(1730099524);
		List<CommentDTO> commentDtos = new LinkedList<>();
    	cListTest.forEach(C -> commentDtos.add(new CommentDTO(C)));
		cListTest.forEach(c1 -> System.out.println(c1));
//		mockMvc.perform(MockMvcRequestBuilders.get("/comment")
//				.contentType("application/json")
//				 .param("id", "true")
//				  .content(objectMapper.writeValueAsString(commentDtos)))
//					.andExpect(status().isAccepted());
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/comment")
    			.contentType("application/json"))
    			.andExpect(status().is4xxClientError());
	}
}
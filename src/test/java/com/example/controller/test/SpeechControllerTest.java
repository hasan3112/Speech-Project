package com.example.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.controller.SpeechController;

@WebMvcTest(value = SpeechController.class)
public class SpeechControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testgenerateResponse() throws Exception{
		
		String url1 = "https://raw.githubusercontent.com/hasan3112/Speech-Project/master/speech-1.csv";
		String url2 = "htps://raw.githubusercontent.com/hasan3112/Speech-Project/master//speech-1.csv";
		String url3 = "https://raw.githubusercontent.com/hasan3112/Speech-Project/master/speech-2.csv";
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/evaluation/?url1="+url1+"&url2="+url2+"&url3="+url3);
		
		ResultActions resultActions = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = resultActions.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		
		assertEquals(200, status);
	}
}

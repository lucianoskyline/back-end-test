package br.com.bb.controller;

import br.com.bb.entity.CategoryEntity;
import br.com.bb.entity.ProductEntity;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import br.com.bb.Application;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerTest {

	@Autowired
    private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void select() throws Exception {
		mockMvc.perform(get("/category/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1)))
				.andExpect(jsonPath("name", is("Alimentos")));
	}

	@Test
	public void selectNonexistentId() throws Exception {
		mockMvc.perform(get("/category/100"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void save() throws Exception {
		CategoryEntity categoryEntity=new CategoryEntity();
		categoryEntity.setName("nova categoria");

		Gson gson=new Gson();
		mockMvc.perform(post("/category")
				.content(gson.toJson(categoryEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", notNullValue()));
	}

	@Test
	public void saveWithNameNull() throws Exception {
		CategoryEntity categoryEntity=new CategoryEntity();
		categoryEntity.setName(null);

		Gson gson=new Gson();
		mockMvc.perform(post("/category")
				.content(gson.toJson(categoryEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void remove() throws Exception {
		mockMvc.perform(delete("/category/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void removeNonexistentId() throws Exception {
		mockMvc.perform(delete("/category/100").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void update() throws Exception {
		CategoryEntity categoryEntity=new CategoryEntity();
		categoryEntity.setId(1);
		categoryEntity.setName("update categoria");

		Gson gson=new Gson();
		mockMvc.perform(put("/category/1")
				.content(gson.toJson(categoryEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name",is("update categoria")))
				.andExpect(jsonPath("id", is(1)));
	}

	@Test
	public void updateWithNameNull() throws Exception {
		CategoryEntity categoryEntity=new CategoryEntity();
		categoryEntity.setId(1);
		categoryEntity.setName(null);

		Gson gson=new Gson();
		mockMvc.perform(put("/category/1")
				.content(gson.toJson(categoryEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateNonexistentId() throws Exception {
		CategoryEntity categoryEntity=new CategoryEntity();
		categoryEntity.setId(1);
		categoryEntity.setName("update categoria");

		Gson gson=new Gson();
		mockMvc.perform(put("/category/100")
				.content(gson.toJson(categoryEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
    public void listAll() throws Exception {
        mockMvc.perform(get("/category/listAll"))
        .andExpect(status().isOk())
	    		.andExpect(jsonPath("$", hasSize(3)))
	        .andExpect(jsonPath("$[0].id", is(1)))
	        .andExpect(jsonPath("$[0].name", is("Alimentos")))
	        .andExpect(jsonPath("$[1].id", is(2)))
	        .andExpect(jsonPath("$[1].name", is("Eletrodomésticos")))
	        .andExpect(jsonPath("$[2].id", is(3)))
	        .andExpect(jsonPath("$[2].name", is("Móveis")));
    }

	@Test
	public void listByOccurrence() throws Exception {
		mockMvc.perform(get("/category/listByOccurrence/o"))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(2)))
				.andExpect(jsonPath("$[0].name", is("Eletrodomésticos")))
				.andExpect(status().isOk());
	}

}

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
import org.springframework.web.context.WebApplicationContext;

import br.com.bb.Application;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductControllerTest {

	@Autowired
    private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void select() throws Exception {
		mockMvc.perform(get("/product/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1)))
				.andExpect(jsonPath("name", is("Arroz")));
	}

	@Test
	public void selectNonexistentId() throws Exception {
		mockMvc.perform(get("/product/100"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void save() throws Exception {
		ProductEntity productEntity=new ProductEntity();
		productEntity.setName("novo produto");
		productEntity.setCategory(new CategoryEntity());
		productEntity.getCategory().setId(1);

		Gson gson=new Gson();
		mockMvc.perform(post("/product")
				.content(gson.toJson(productEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", notNullValue()));
	}

	@Test
	public void saveWithCategoryNull() throws Exception {
		ProductEntity productEntity=new ProductEntity();
		productEntity.setName("novo produto");

		Gson gson=new Gson();
		mockMvc.perform(post("/product")
				.content(gson.toJson(productEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void remove() throws Exception {
		mockMvc.perform(delete("/product/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void removeNonexistentId() throws Exception {
		mockMvc.perform(delete("/product/100").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void update() throws Exception {
		ProductEntity productEntity=new ProductEntity();
		productEntity.setId(1);
		productEntity.setName("update produto");
		productEntity.setCategory(new CategoryEntity());
		productEntity.getCategory().setId(1);

		Gson gson=new Gson();
		mockMvc.perform(put("/product/1")
				.content(gson.toJson(productEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name",is("update produto")))
				.andExpect(jsonPath("id", is(1)));
	}

	@Test
	public void updateWithCategoryNull() throws Exception {
		ProductEntity productEntity=new ProductEntity();
		productEntity.setId(1);
		productEntity.setName("update produto");

		Gson gson=new Gson();
		mockMvc.perform(put("/product/1")
				.content(gson.toJson(productEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateNonexistentId() throws Exception {
		ProductEntity productEntity=new ProductEntity();
		productEntity.setId(100);
		productEntity.setName("update produto");

		Gson gson=new Gson();
		mockMvc.perform(put("/product/100")
				.content(gson.toJson(productEntity))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void listAll() throws Exception {
		mockMvc.perform(get("/product/listAll"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(8)));
	}

	@Test
	public void listByCategoryNonexistentCategoryId() throws Exception {
		mockMvc.perform(get("/product/listByCategory/100"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void listByCategoryAlimentos() throws Exception {
        mockMvc.perform(get("/product/listByCategory/1"))
        .andExpect(status().isOk())
	    		.andExpect(jsonPath("$", hasSize(2)))
	        .andExpect(jsonPath("$[0].id", is(1)))
	        .andExpect(jsonPath("$[0].name", is("Arroz")))
	        .andExpect(jsonPath("$[1].id", is(2)))
	        .andExpect(jsonPath("$[1].name", is("Feijão")));
    }

	@Test
	public void listByCategoryEletrodomesticos() throws Exception {
		mockMvc.perform(get("/product/listByCategory/2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[0].id", is(3)))
		.andExpect(jsonPath("$[0].name", is("Aspirador de pó")))
		.andExpect(jsonPath("$[1].id", is(4)))
		.andExpect(jsonPath("$[1].name", is("Batedeira")))
		.andExpect(jsonPath("$[2].id", is(5)))
		.andExpect(jsonPath("$[2].name", is("Liquidificador")));
	}

	@Test
	public void listByCategoryMoveis() throws Exception {
		mockMvc.perform(get("/product/listByCategory/3"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[0].id", is(6)))
		.andExpect(jsonPath("$[0].name", is("Sofá")))
		.andExpect(jsonPath("$[1].id", is(7)))
		.andExpect(jsonPath("$[1].name", is("Mesa")))
		.andExpect(jsonPath("$[2].id", is(8)))
		.andExpect(jsonPath("$[2].name", is("Estante")));
	}
}

package br.com.bb.controller;

import br.com.bb.entity.CategoryEntity;
import br.com.bb.entity.ProductEntity;
import br.com.bb.repository.CategoryRepository;
import br.com.bb.repository.ProductRepository;
import br.com.bb.validate.ValidateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 30/01/2019.
 */

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private ValidateEntity validateEntity=new ValidateEntity();

    @GetMapping("/listAll")
    public ResponseEntity<?> list(){
        return new ResponseEntity<List>(productRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Integer id){
        ProductEntity productEntity=productRepository.findOne(id);
        if(productEntity!=null){
            return ResponseEntity.ok().body(productEntity);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity save(@Validated @RequestBody ProductEntity productEntity, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validateEntity.showErros(bindingResult));
        }

        return ResponseEntity.ok().body(productRepository.save(productEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @Validated @RequestBody ProductEntity productEntity, BindingResult bindingResult){
        if(productRepository.findOne(id)==null){
            return ResponseEntity.badRequest().build();
        }

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validateEntity.showErros(bindingResult));
        }
        productEntity.setId(id);
        return ResponseEntity.ok().body(productRepository.save(productEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        ProductEntity productEntity = productRepository.findOne(id);
        if(productEntity==null){
            return ResponseEntity.badRequest().build();
        }
        productRepository.delete(productEntity);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/listByCategory/{id}")
    public ResponseEntity<?> findByCategoryId(@PathVariable Integer id){
        CategoryEntity categoryEntity=categoryRepository.findOne(id);
        if(categoryEntity!=null){
            List<ProductEntity> products=productRepository.findByCategory(categoryEntity);
            return ResponseEntity.ok().body(products);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

}

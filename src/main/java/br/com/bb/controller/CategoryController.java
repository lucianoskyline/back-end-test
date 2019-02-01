package br.com.bb.controller;

import br.com.bb.entity.CategoryEntity;
import br.com.bb.entity.ProductEntity;
import br.com.bb.repository.CategoryRepository;
import br.com.bb.validate.ValidateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by luciano on 31/01/2019.
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    private ValidateEntity validateEntity=new ValidateEntity();

    @GetMapping("/listAll")
    public ResponseEntity<?> list(){
        return new ResponseEntity<List>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/listByOccurrence/{name}")
    public ResponseEntity<?> listByOccurrence(@PathVariable String name){
        return new ResponseEntity<List>(categoryRepository.findByOccurrence(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Integer id){
        CategoryEntity categoryEntity=categoryRepository.findOne(id);
        if(categoryEntity!=null){
            return ResponseEntity.ok().body(categoryEntity);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity save(@Validated @RequestBody CategoryEntity categoryEntity, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validateEntity.showErros(bindingResult));
        }

        return ResponseEntity.ok().body(categoryRepository.save(categoryEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @Validated @RequestBody CategoryEntity categoryEntity, BindingResult bindingResult){
        if(categoryRepository.findOne(id)==null){
            return ResponseEntity.badRequest().build();
        }

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validateEntity.showErros(bindingResult));
        }
        categoryEntity.setId(id);
        return ResponseEntity.ok().body(categoryRepository.save(categoryEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        CategoryEntity categoryEntity = categoryRepository.findOne(id);
        if(categoryEntity==null){
            return ResponseEntity.badRequest().build();
        }
        try{
            categoryRepository.delete(categoryEntity);
        }
        catch (Exception error){
            error.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }


}

package br.com.bb.repository;

import br.com.bb.entity.CategoryEntity;
import br.com.bb.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by luciano on 30/01/2019.
 */
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    List<ProductEntity> findByCategory(CategoryEntity categoryEntity);

}

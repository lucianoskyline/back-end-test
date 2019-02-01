package br.com.bb.repository;

import br.com.bb.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by luciano on 30/01/2019.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    @Query(value = "SELECT *, ROUND ((LENGTH(name) - LENGTH( REPLACE ( name,:parameter,'') ) ) / LENGTH(:parameter)) AS total " +
            "FROM category order by total desc limit 1", nativeQuery = true)
    List<CategoryEntity> findByOccurrence(@Param("parameter") String parameter);

}

package br.com.bb.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by luciano on 30/01/2019.
 */

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull(message = "O campo nome deve ser preenchido")
    @Size(min=4, max = 80, message = "O campo nome deve ter ao menos 4 caracteres")
    private String name;
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id")
    @NotNull(message = "Informe a categoria")
    private CategoryEntity category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}

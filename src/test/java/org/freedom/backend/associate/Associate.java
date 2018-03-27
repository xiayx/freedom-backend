package org.freedom.backend.associate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 关联对象
 *
 * @author xiayx
 */
//tag::entity[]
@Entity
public class Associate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //tag::entity[]


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //tag::entity[]
}
//tag::entity[]
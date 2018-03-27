package org.freedom.backend.associate;

import javax.persistence.*;
import java.util.List;

/**
 * 主对象
 *
 * @author xiayx
 */
//tag::entity[]
@Entity
public class Main {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 关联主键 */
    private Long associateId;
    /** 关联对象 */
    @Transient
    private Associate associate;
    /** 关联主键集合 */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    private List<Long> associateIds;
    /** 关联对象集合 */
    @Transient
    private List<Associate> associates;
    //tag::entity[]

    public Main() {
    }

    public Main(Long associateId) {
        this.associateId = associateId;
    }

    public Main(Long associateId, List<Long> associateIds) {
        this.associateId = associateId;
        this.associateIds = associateIds;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public Associate getAssociate() {
        return associate;
    }

    public void setAssociate(Associate associate) {
        this.associate = associate;
    }

    public List<Long> getAssociateIds() {
        return associateIds;
    }

    public void setAssociateIds(List<Long> associateIds) {
        this.associateIds = associateIds;
    }

    public List<Associate> getAssociates() {
        return associates;
    }

    public void setAssociates(List<Associate> associates) {
        this.associates = associates;
    }
    //tag::entity[]
}
//tag::entity[]

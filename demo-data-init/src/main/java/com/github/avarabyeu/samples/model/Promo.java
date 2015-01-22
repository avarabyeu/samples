package com.github.avarabyeu.samples.model;

import javax.persistence.*;

/**
 * Created by andrey.vorobyov on 1/20/15.
 */
@Entity
@Table(name = "promo")
public class Promo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "label")
    private String label;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "promo")
    private Title title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }
}

package com.github.avarabyeu.samples.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by andrey.vorobyov on 1/20/15.
 */
@Entity
@Table(name = "title")
public class Title {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "type")
    private String type;

    @Column(name = "format")
    private String format;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_id")
    private Promo promo;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    @Override
    public String toString() {
        return "Title{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", format='" + format + '\'' +
                ", date=" + date +
                ", person=" + person +
                ", promo=" + promo +
                '}';
    }
}

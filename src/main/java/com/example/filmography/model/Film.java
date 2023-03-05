package com.example.filmography.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film extends GenericModel{

    @Column(name = "title")
    private String title;

    @Column(name = "premier_year")
    private String premierYear;

    @Column(name = "country")
    private String country;

    @Enumerated
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "amount")
    private Integer amount;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "films_id"),
            foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
            inverseJoinColumns = @JoinColumn(name = "directors_id"),
            inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS")
    )
    private Set<Director> directors = new HashSet<>();
}

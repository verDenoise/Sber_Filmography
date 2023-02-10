package com.example.filmography.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Director extends GenericModel {

    @Column(name = "directors_fio")
    private String directorFIO;

    @Column(name = "position")
    private String position;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "directors_id"),
            foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
            inverseJoinColumns = @JoinColumn(name = "films_id"),
            inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS")
    )
    private Set<Film> films = new HashSet<>();

}

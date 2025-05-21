package com.zakaria.streamingPlatform.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "genres")
public class GenresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<MovieEntity> movie;

    public GenresEntity() {
    }

    public GenresEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

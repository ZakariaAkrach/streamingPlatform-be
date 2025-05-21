package com.zakaria.streamingPlatform.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cast")
public class CastEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String original_name;
    private String characterName;
    private String profile_path;

    @ManyToMany(mappedBy = "cast")
    private List<MovieEntity> movie;

    public CastEntity() {
    }

    public CastEntity(Long id, String name, String original_name, String characterName, String profile_path) {
        this.id = id;
        this.name = name;
        this.original_name = original_name;
        this.characterName = characterName;
        this.profile_path = profile_path;
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

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}

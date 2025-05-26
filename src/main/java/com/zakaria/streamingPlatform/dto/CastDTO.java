package com.zakaria.streamingPlatform.dto;

public class CastDTO {
    private Long id;
    private String name;
    private String original_name;
    private String profile_path;

    public CastDTO() {
    }

    public CastDTO(Long id, String name, String original_name, String profile_path) {
        this.id = id;
        this.name = name;
        this.original_name = original_name;
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

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}

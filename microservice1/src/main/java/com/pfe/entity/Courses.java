package com.pfe.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity

public class Courses {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private int duration;
    private String difficulty;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;

    public List<CourseFile> getFiles() {
        return files;
    }

    public void setFiles(List<CourseFile> files) {
        this.files = files;
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseFile> files = new ArrayList<>();
}

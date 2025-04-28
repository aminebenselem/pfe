package com.pfe.controller;

import com.pfe.dto.CousesDto;
import com.pfe.entity.CourseFile;
import com.pfe.entity.Courses;
import com.pfe.repository.CategoryRepository;
import com.pfe.repository.CourcesRepository;
import com.pfe.repository.CourseFileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CoursesController {
    private final CourcesRepository courcesRepository;
    private final CourseFileRepository courseFileRepository;
    private  final CategoryRepository categoryRepository;
    public CoursesController(CourcesRepository courcesRepository, CourseFileRepository courseFileRepository, CategoryRepository categoryRepository) {
        this.courcesRepository = courcesRepository;
        this.courseFileRepository = courseFileRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/addcourse")
    public ResponseEntity<Courses> addCourse(@RequestBody CousesDto course) {
        Courses courses = new Courses();
        List<CourseFile> courseFiles = new ArrayList<>();

        course.files.forEach(file -> {
            CourseFile courseFile = new CourseFile();
            courseFile.setFilePath(file);
            courseFile.setCourse(courses);
            courseFiles.add(courseFile);
        });

        courses.setCategory(categoryRepository.findById(course.category.getId()).get());
        courses.setDescription(course.description);
        courses.setDifficulty(course.difficulty);
        courses.setTitle(course.title);
        courses.setFiles(courseFiles);
        courses.setDuration(course.duration);

        Courses saved = courcesRepository.save(courses);  // this will save both the course and its files
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }


    @GetMapping("/getCourses")
    public ResponseEntity<List<Courses>> getCourses(){
        return new ResponseEntity<>(courcesRepository.findAll(), HttpStatus.OK);
    }
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        Optional<Courses> courseOptional = courcesRepository.findById(id);

        if (courseOptional.isPresent()) {
            Courses course = courseOptional.get();

            File uploadsFolder = new File("uploads");  // same as in your upload controller!

            course.getFiles().forEach(courseFile -> {
                String url = courseFile.getFilePath();
                String filename = url.substring(url.lastIndexOf('/') + 1);

                File physicalFile = new File(uploadsFolder, filename);
                if (physicalFile.exists()) {
                    boolean deleted = physicalFile.delete();
                    if (!deleted) {
                        System.out.println("⚠️ Failed to delete: " + physicalFile.getAbsolutePath());
                    } else {
                        System.out.println("✅ Deleted: " + physicalFile.getAbsolutePath());
                    }
                } else {
                    System.out.println("⚠️ File not found: " + physicalFile.getAbsolutePath());
                }
            });

            courcesRepository.delete(course);

            return ResponseEntity.ok("Course and its files were deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
    }
    @GetMapping("/getCourse/{id}")
    public ResponseEntity<Courses> getCourseById(@PathVariable Long id) {
        return courcesRepository.findById(id)
                .map(course -> new ResponseEntity<>(course, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

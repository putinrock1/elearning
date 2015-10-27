package thecerealkillers.elearning.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thecerealkillers.elearning.model.Course;

import java.util.List;

@RestController
public interface CoursesController {

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    ResponseEntity<List<Course>> getAllCourses();

    @RequestMapping(value = "/courses", method = RequestMethod.POST)
    ResponseEntity createCourse(@RequestBody Course course);

    @RequestMapping(value = "/courses", method = RequestMethod.DELETE)
    ResponseEntity deleteCourse(@RequestParam(value = "title", required = true) String title);

    @RequestMapping(value = "/courses/{title}", method = RequestMethod.GET)
    ResponseEntity<Course> getCourse(@PathVariable("title") String title);
}

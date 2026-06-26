package edu.springorm.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.springorm.demo.model.Student;
import edu.springorm.demo.repository.StudentRepository;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
// ✅ Added explicit CORS permission for localhost ports and your live Vercel production link
@CrossOrigin(origins = {
    "http://localhost:3000", 
    "http://localhost:3001", 
    "http://localhost:3002", 
    "https://ticket-booking-app-tawny.vercel.app"
}) 
public class StudentController {

    private final StudentRepository repo;

    @Autowired
    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    // REGISTER Student Accounts with JSON validations
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        Student existing = repo.findByUsername(student.getUsername());

        if (existing != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "User already exists"));
        }

        repo.save(student);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Registered Successfully"));
    }

    // LOGIN Authentication Pipeline Checking
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student student) {
        Student existing = repo.findByUsername(student.getUsername());

        if (existing != null && existing.getPassword().equals(student.getPassword())) {
            return ResponseEntity.ok(existing);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid Student Credentials"));
    }

    // GET ALL Active Student Profiles array
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(repo.findAll());
    }
}
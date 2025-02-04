package com.example.student_service.service;
import com.example.student_service.dto.StudentWithSchool;
import com.example.student_service.entity.Student;
import com.example.student_service.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RestTemplate restTemplate; // To call School Microservice

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }

    public Object getStudentWithSchool(String studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            // Call School Microservice to get school details
            String schoolServiceUrl = "http://localhost:8080/api/schools/" + student.getSchoolId();
            Object school = restTemplate.getForObject(schoolServiceUrl, Object.class);

            return new StudentWithSchool(student, school);
        }

        return null;
    }
}
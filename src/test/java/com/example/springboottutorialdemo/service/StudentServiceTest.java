package com.example.springboottutorialdemo.service;

import com.example.springboottutorialdemo.entity.StudentEntity;
import com.example.springboottutorialdemo.exception.StudentNotFoundException;
import com.example.springboottutorialdemo.repository.StudentRepository;
import org.hamcrest.text.IsEqualCompressingWhiteSpace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @DisplayName("This tests that if student id is existing, getStudentById will return the student entity")
    public void testGetStudentById_Success() {
        //given: student_id is existing
        int existing_student_id = 1;
        StudentEntity studentEntity = new StudentEntity(1,"Test Name",1, "Test Address");
        given(studentRepository.findById(existing_student_id)).willReturn(Optional.of(studentEntity));
        //when: studentService.getStudentById is executed
        StudentEntity studentServicesResult = studentService.getStudentById(existing_student_id);
        //then: return of studentService.getStudentById should be equal to return of studentRepository.findById
        assertEquals(studentServicesResult, studentEntity);
    }

    @Test
    @DisplayName("This tests that if student id is non-existing, getStudentById will throw StudentNotFoundException")
    public void testGetStudentById_Fail() {
        //given: student_id is non-existing
        int non_existing_student_id = 1;
        given(studentRepository.findById(non_existing_student_id)).willThrow(new StudentNotFoundException("Student with id : " + non_existing_student_id + " doesn't exist."));
        //when: studentService.getStudentById is executed
        StudentNotFoundException result = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(non_existing_student_id);
        });
        //then: studentService.getStudentById will throw a StudentNotFoundException with message "Student with id : <non_existing student_id> doesn't exist."
        assertEquals("Student with id : 1 doesn't exist.", result.getMessage());
    }

    @Test
    @DisplayName("This tests that given a student entity, when addStudent method is executed, it should return new student entity")
    public  void testAddStudent(){
        StudentEntity newStudent = new StudentEntity(1,"Test Name", 1, "Test Address");
        given(studentRepository.save(newStudent)).willReturn(newStudent);
        StudentEntity result=studentService.addStudent(newStudent);
        assertEquals(newStudent, result);
    }

    @Test
    @DisplayName("This tests that if student id is existing, deleteStudentById will return null")
    public void testDeleteStudentById_Success (){
        int existing_student_id = 1;
        StudentEntity deleteStudent = new StudentEntity(1,"Test Name",1, "Test Address");
        given(studentRepository.findById(existing_student_id)).willReturn(Optional.of(deleteStudent));
        StudentEntity result = studentService.deleteStudentById(existing_student_id);

        assertEquals(null, result);
        verify(studentRepository, times(1)).delete(deleteStudent);
    }

    @Test
    @DisplayName("This tests that if student id is non existing, deleteStudentById will  throw a StudentNotFoundException exception with message, \"Student with id : <non-existing_id> doesn't exist.\"")
    public void testDeleteStudentById_Fail (){
        int non_existing_student_id = 1;
        given(studentRepository.findById(non_existing_student_id)).willThrow(new StudentNotFoundException("Student with id : " + non_existing_student_id + " doesn't exist."));
        StudentNotFoundException result =assertThrows(StudentNotFoundException.class, () -> {
            studentService.deleteStudentById(non_existing_student_id);
        });
        assertEquals("Student with id : 1 doesn't exist.", result.getMessage());
        //verify(studentRepository, times(1)).delete(deleteStudent);
    }

}
package org.example;

import com.learnhibernate.entities.Certificates;
import com.learnhibernate.entities.Student;
import com.learnhibernate.services.StudentHQLService;
import com.learnhibernate.services.StudentService;
import learnhibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Student student = new Student();

//        student.setName("Ashish");
//        student.setActive(false);
//        student.setAbout("Driver");
//        student.setFatherName("Papa");
//        student.setCollege("CU");
//        student.setPhone("123456890");
////
//        Certificates c1 = new Certificates();
//        c1.setCertificateLink("java.pdf");
////
//        Certificates c2 = new Certificates();
//        c2.setCertificateLink("hibernate.pdf");
////
//        StudentService ss = new StudentService();
//        ss.saveStudent(student, c1, c2);

//        ss.deleteStudent(102);

//        student.setName("Khushi");
//        ss.updateStudent(student, 202);
//
//        System.out.println(ss.getByRollNo(202).getName());

        StudentHQLService shs = new StudentHQLService();
        List<Student> students = shs.getAllStudentsWithCertificates();

        for(Student stu: students){
            System.out.println(stu.getName());
            if(stu.getCertificatesList().size() == 0){
                System.out.println("No certificates found");
            }
            for(Certificates certificate : stu.getCertificatesList()){
                System.out.println(certificate.getCertificateLink());
            }
        }
    }
}

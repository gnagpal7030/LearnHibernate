package com.learnhibernate.services;

import com.learnhibernate.entities.*;
import org.hibernate.SessionFactory;
import learnhibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class StudentService {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void saveStudent(Student student, Certificates... certificates) {

        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();

            // Set parent reference in every certificate
            if (certificates != null) {

                for (Certificates certificate : certificates) {
                    student.getCertificatesList().add(certificate);
                    certificate.setStudent(student);
                }

//                student.setCertificatesList(Arrays.asList(certificates));
            }

            // Save student
            // Certificates will automatically save because of CascadeType.ALL
            session.persist(student);

            transaction.commit();

            System.out.println("Student saved successfully");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Student getByRollNo(int rollNo) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Student.class, rollNo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateStudent(Student student, int rollNo) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();

            // fetch the old student first
            Student existingStudent = session.get(Student.class, rollNo);
            if (existingStudent != null) {
                // update
                existingStudent.setName(student.getName());
            }
            session.merge(existingStudent);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int rollNo) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();

            // will remove the certificatest too as cascade effect is on.
            session.remove(session.get(Student.class, rollNo));
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

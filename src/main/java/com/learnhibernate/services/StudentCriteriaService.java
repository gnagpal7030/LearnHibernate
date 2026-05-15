package com.learnhibernate.services;

import com.learnhibernate.entities.Student;
import learnhibernate.util.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class StudentCriteriaService {

    private SessionFactory sessionFactory =
            HibernateUtil.getSessionFactory();

    /*
    |--------------------------------------------------------------------------
    | Save Student
    |--------------------------------------------------------------------------
    |
    | Criteria API does not support INSERT operation directly.
    | So we still use session.persist().
    |
    */
    public void saveStudent(Student student) {

        try (Session session = sessionFactory.openSession()) {

            Transaction tx = session.beginTransaction();

            session.persist(student);

            tx.commit();

            System.out.println("Student Saved Successfully");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Get Student By Roll Number
    |--------------------------------------------------------------------------
    */
    // Declares a public method that searches for and returns a single Student object based on a roll number.
    public Student getByRollNo(int rollNo) {

        // Opens a short-lived database Session wrapper.
        // The try-with-resources statement ensures the session auto-closes when the block finishes, preventing memory leaks.
        try (Session session = sessionFactory.openSession()) {

            // Obtains the CriteriaBuilder factory object from the session.
            // This factory is used to construct strongly-typed query components, expressions, and filtering conditions.
            CriteriaBuilder cb =
                    session.getCriteriaBuilder();

            // Creates a CriteriaQuery container object specifically configured to return 'Student' entity objects.
            CriteriaQuery<Student> cq =
                    cb.createQuery(Student.class);

            // Defines the root entity for the query (equivalent to the 'FROM Student' clause in SQL/HQL).
            // The 'root' reference allows you to access and reference individual properties of the Student class.
            Root<Student> root =
                    cq.from(Student.class);

            // Constructs and executes the query logic:
            // 1. cq.select(root) specifies that we want to retrieve the whole Student entity object.
            // 2. cb.equal(...) creates a conditional restriction: where the 'rollNo' attribute matches the passed method argument.
            // 3. .where(...) applies that conditional restriction directly to the main query structure.
            cq.select(root)
                    .where(cb.equal(root.get("rollNo"), rollNo));

            // Translates the criteria object framework into an executable Hibernate Query instance.
            // .uniqueResult() executes the query and expects exactly zero or one record; returns null if empty, or throws an exception if multiple matches are found.
            return session.createQuery(cq)
                    .uniqueResult();

        } catch (Exception e) {

            // Prints the exact code execution error path details to the system console if a database or mapping error occurs.
            e.printStackTrace();

            // Returns null to the calling class indicating that the student record could not be successfully retrieved.
            return null;
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Update Student Name
    |--------------------------------------------------------------------------
    */
    public void updateStudentName(int rollNo,
                                  String newName) {

        try (Session session = sessionFactory.openSession()) {

            Transaction tx = session.beginTransaction();

            CriteriaBuilder cb =
                    session.getCriteriaBuilder();

            CriteriaUpdate<Student> cu =
                    cb.createCriteriaUpdate(Student.class);

            Root<Student> root =
                    cu.from(Student.class);

            cu.set("name", newName)
                    .where(cb.equal(root.get("rollNo"), rollNo));

            int rows =
                    session.createMutationQuery(cu)
                            .executeUpdate();

            tx.commit();

            System.out.println(rows + " row updated");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Delete Student
    |--------------------------------------------------------------------------
    */
    public void deleteStudent(int rollNo) {

        try (Session session = sessionFactory.openSession()) {

            Transaction tx = session.beginTransaction();

            CriteriaBuilder cb =
                    session.getCriteriaBuilder();

            CriteriaDelete<Student> cd =
                    cb.createCriteriaDelete(Student.class);

            Root<Student> root =
                    cd.from(Student.class);

            cd.where(
                    cb.equal(root.get("rollNo"), rollNo)
            );

            int rows =
                    session.createMutationQuery(cd)
                            .executeUpdate();

            tx.commit();

            System.out.println(rows + " row deleted");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Fetch All Students
    |--------------------------------------------------------------------------
    */
    public List<Student> getAllStudents() {

        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder cb =
                    session.getCriteriaBuilder();

            CriteriaQuery<Student> cq =
                    cb.createQuery(Student.class);

            Root<Student> root =
                    cq.from(Student.class);

            cq.select(root);

            return session.createQuery(cq)
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Fetch All Students With Certificates
    |--------------------------------------------------------------------------
    |
    | fetch() performs JOIN FETCH equivalent.
    | distinct(true) removes duplicate students
    | caused by OneToMany joins.
    |
    */
    public List<Student> getAllStudentsWithCertificates() {

        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder cb =
                    session.getCriteriaBuilder();

            CriteriaQuery<Student> cq =
                    cb.createQuery(Student.class);

            Root<Student> root =
                    cq.from(Student.class);

            // JOIN FETCH certificates
            Fetch<Object, Object> fetch =
                    root.fetch(
                            "certificatesList",
                            JoinType.LEFT
                    );

            cq.select(root)
                    .distinct(true);

            return session.createQuery(cq)
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}


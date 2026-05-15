package com.learnhibernate.services;

import com.learnhibernate.entities.Student;
import learnhibernate.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentHQLService {

    private SessionFactory sessionFactory =
            HibernateUtil.getSessionFactory();

    /*
    |--------------------------------------------------------------------------
    | Save Student using HQL
    |--------------------------------------------------------------------------
    |
    | HQL does not support INSERT directly like SQL.
    | So for insertion we still use session.persist().
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
    | Get Student By Roll Number using HQL
    |--------------------------------------------------------------------------
    */
    public Student getByRollNo(int rollNo) {

        try (Session session = sessionFactory.openSession()) {

            String hql =
                    "FROM Student WHERE rollNo = :rollNo";

            Query<Student> query =
                    session.createQuery(hql, Student.class);

            query.setParameter("rollNo", rollNo);

            return query.uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Update Student Name using HQL
    |--------------------------------------------------------------------------
    */
    public void updateStudentName(int rollNo, String newName) {

        try (Session session = sessionFactory.openSession()) {

            Transaction tx = session.beginTransaction();

            String hql =
                    "UPDATE Student " +
                            "SET name = :name " +
                            "WHERE rollNo = :rollNo";

            Query query = session.createQuery(hql);

            query.setParameter("name", newName);
            query.setParameter("rollNo", rollNo);

            int rows = query.executeUpdate();

            tx.commit();

            System.out.println(rows + " row updated");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Delete Student using HQL
    |--------------------------------------------------------------------------
    */
    public void deleteStudent(int rollNo) {

        try (Session session = sessionFactory.openSession()) {

            Transaction tx = session.beginTransaction();

            String hql =
                    "DELETE FROM Student " +
                            "WHERE rollNo = :rollNo";

            Query query = session.createQuery(hql);

            query.setParameter("rollNo", rollNo);

            int rows = query.executeUpdate();

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

            String hql = "FROM Student";

            Query<Student> query =
                    session.createQuery(hql, Student.class);

            return query.getResultList();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Fetch All Students with Certificates
    |--------------------------------------------------------------------------
    |
    | JOIN FETCH loads certificates together with students
    | and avoids LazyInitializationException.
    |
    By default, Hibernate uses Lazy Loading for associated collections (like certificatesList).Without FETCH: Hibernate only loads the basic Student data. The certificatesList is replaced with a temporary proxy object. If your application tries to read certificates after the database Session is closed (outside the try block), Hibernate throws a LazyInitializationException.
    With FETCH: JOIN FETCH forces Hibernate to eagerly load the Student records and their associated certificatesList from
     the database together in a single operation, while the Session is still wide open.

     Because a LEFT JOIN returns a flat database result set, a student with 3 certificates will occupy 3 raw rows in the SQL response. The DISTINCT keyword in your HQL tells Hibernate to de-duplicate those rows in memory, returning a clean, uniquely mapped List<Student> where each student object appears exactly once.

     The query knows which table to target because it looks at the Java object mapping inside your Student class, not the database itself.
Hibernate relies entirely on the Metadata (Annotations) you provided in your source code.
    */
    public List<Student> getAllStudentsWithCertificates() {

        try (Session session = sessionFactory.openSession()) {

            String hql =
                    "SELECT DISTINCT s " +
                            "FROM Student s " +
                            "LEFT JOIN FETCH s.certificatesList";

            Query<Student> query =
                    session.createQuery(hql, Student.class);

            return query.getResultList();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    public List<Student> getPaginatedStudents(int pageNumber, int pageSize) {

        try (Session session = sessionFactory.openSession()) {

            String hql = "FROM Student s ORDER BY s.rollNo ASC";

            Query<Student> query = session.createQuery(hql, Student.class);

            // Calculate the starting position (OFFSET)
            int offset = (pageNumber - 1) * pageSize;
            query.setFirstResult(offset);

            // Set the page size (LIMIT)
            query.setMaxResults(pageSize);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
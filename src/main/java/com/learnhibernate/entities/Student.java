// Declares the package matching the folder structure for this entity
package com.learnhibernate.entities;

// Imports all standard Jakarta Persistence API annotations for database mapping

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// Specifies that this class is a JPA entity mapped to a database table
@Entity
// Overrides the default table name to explicitly map this class to the "students" table
@Table(name = "students")
public class Student {

    // Marks this specific field as the primary key of the table
    @Id
    // Configures Hibernate to automatically generate unique primary key values
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int rollNo;

    // Configures database constraints: custom column name, max length 100, and no duplicates allowed
    @Column(name = "student_name", length = 100, unique = true)
    private String name;

    // Configures database constraints: custom column name, max length 200, and allows null values
    @Column(name = "college_name", length = 200, nullable = true)
    private String college;

    // Maps automatically to a standard database column named "phone"
    private String phone;

    // Maps automatically to a standard database column named "fatherName"
    private String fatherName;

    // Maps automatically to a boolean column, defaulting to true for new instances
    private boolean active = true;

    // Marks this field as a Large Object to store long text (maps to TEXT/CLOB in database)
    @Lob
    private String about;

    /*
 |--------------------------------------------------------------------------
 | @OneToMany Relationship Explanation
 |--------------------------------------------------------------------------
 |
 | This annotation defines a ONE-TO-MANY relationship between:
 |
 |      One Student  --->  Multiple Certificates
 |
 | Meaning:
 | A single student can have many certificates associated with it.
 |
 |-------------------------------------------------------------------------
 | mappedBy = "student"
 |--------------------------------------------------------------------------
 |
 | Indicates that the relationship is managed by the "student" field
 | present inside the Certificates entity.
 |
 | Hibernate will NOT create an extra join table because the foreign key
 | already exists in the Certificates table.
 |
 | Owner Side:
 |
 |     @ManyToOne
 |     @JoinColumn(name = "rollNo")
 |     private Student student;
 |
 | Here, Certificates is the OWNER side because it contains the foreign key.
 |
 | Student side becomes the INVERSE/NON-OWNING side.
 |
 |--------------------------------------------------------------------------
 | fetch = FetchType.LAZY
 |--------------------------------------------------------------------------
 |
 | LAZY loading means related certificates are NOT loaded immediately
 | when Student is fetched from database.
 |
 | Example:
 |
 |     Student s = session.get(Student.class, 1);
 |
 | Only student data loads initially.
 |
 | Certificates load ONLY when:
 |
 |     s.getCertificatesList();
 |
 | is accessed.
 |
 | Benefit:
 | Improves performance by avoiding unnecessary database queries.
 |
 |--------------------------------------------------------------------------
 | cascade = CascadeType.ALL
 |--------------------------------------------------------------------------
 |
 | Any operation performed on Student automatically applies
 | to Certificates as well.
 |
 | Includes:
 |
 | 1. PERSIST  -> save child automatically
 | 2. MERGE    -> update child automatically
 | 3. REMOVE   -> delete child automatically
 | 4. REFRESH  -> refresh child
 | 5. DETACH   -> detach child
 |
 | Example:
 |
 |     session.persist(student);
 |
 | automatically saves all certificates too.
 |
 | Example:
 |
 |     session.remove(student);
 |
 | automatically deletes all certificates related to that student.
 |
 |--------------------------------------------------------------------------
 | orphanRemoval = true
 |--------------------------------------------------------------------------
 |
 | If a certificate is removed from the certificates list,
 | Hibernate automatically deletes it from database.
 |
 | Example:
 |
 |     student.getCertificatesList().remove(c1);
 |
 | Hibernate executes DELETE query for that certificate.
 |
 | Without orphanRemoval:
 | Certificate would remain in DB as orphan/unlinked data.
 |
 |--------------------------------------------------------------------------
 | Database Structure
 |--------------------------------------------------------------------------
 |
// | students table
// | ----------------
 | rollNo (PK)
 |
 | certificates table
 | -------------------
 | certificateID (PK)
 | rollNo (FK)
 |
 | Foreign key "rollNo" connects Certificates to Student.
 |
 */
    @OneToMany(
            mappedBy = "student",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Certificates> certificatesList = new ArrayList<>();

    public List<Certificates> getCertificatesList() {
        return certificatesList;
    }

    public void setCertificatesList(List<Certificates> certificatesList) {
        this.certificatesList = certificatesList;
    }

    // Getter method to retrieve the student's roll number
    public int getRollNo() {
        return rollNo;
    }

    // Setter method to assign a roll number to the student
    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    // Getter method to retrieve the student's name
    public String getName() {
        return name;
    }

    // Setter method to assign a name to the student
    public void setName(String name) {
        this.name = name;
    }

    // Getter method to retrieve the college name
    public String getCollege() {
        return college;
    }

    // Setter method to assign a college name
    public void setCollege(String college) {
        this.college = college;
    }

    // Getter method to retrieve the student's phone number
    public String getPhone() {
        return phone;
    }

    // Setter method to assign a phone number to the student
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter method to retrieve the father's name
    public String getFatherName() {
        return fatherName;
    }

    // Setter method to assign a father's name to the student
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    // Getter method to check if the student profile is active
    public boolean isActive() {
        return active;
    }

    // Setter method to toggle the student's active status
    public void setActive(boolean active) {
        this.active = active;
    }

    // Getter method to retrieve the detailed text about the student
    public String getAbout() {
        return about;
    }

    // Setter method to store detailed text about the student
    public void setAbout(String about) {
        this.about = about;
    }
}

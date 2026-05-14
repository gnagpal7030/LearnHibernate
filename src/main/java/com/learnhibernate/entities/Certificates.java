package com.learnhibernate.entities;

import jakarta.persistence.*;

@Entity
public class Certificates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int certificateID;

    public String getCertificateLink() {
        return certificateLink;
    }

    public void setCertificateLink(String certificateLink) {
        this.certificateLink = certificateLink;
    }

    public int getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(int certificateID) {
        this.certificateID = certificateID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    private String certificateLink;

    @ManyToOne
    @JoinColumn(name="rollNo")
    private Student student;
}

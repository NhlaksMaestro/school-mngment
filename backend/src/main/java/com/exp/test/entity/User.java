package com.exp.test.entity;

import java.sql.Date;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email_address", nullable = false)
  private String email;

  @Column(name = "cellphone_number", nullable = false)
  private String cellphone;

  @Column(name = "student_number", nullable = false)
  private String studentNumber;

  @Column(name = "current_score", nullable = false)
  private Integer currScore;

  @Column(name = "average_score", nullable = false)
  private Integer aveScore;

  @Column(name = "date_of_birth", nullable = false)
  private Date dateOfBirth;

  @Column(name = "created_by", nullable = false)
  private String createdBy;
  
  @Column(name = "created_on", nullable = false)
  private Date createdOn;

  @Column(name = "updated_by", nullable = false)
  private String updatedBy;

  @Column(name = "updated_on", nullable = false)
  private Date updatedOn;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted;
}
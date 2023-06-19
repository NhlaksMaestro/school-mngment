package com.exp.test.dto;

import lombok.Data;

import java.sql.Date;

import org.springframework.beans.BeanUtils;

@Data
public class User {
  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
  private String studentNumber;
  private Date dateOfBirth;
  private String cellphone;
  private Integer currScore;
  private Integer aveScore;
  private String createdBy;
  private Date createdOn;
  private String updatedBy;
  private Date updatedOn;
  private Boolean isDeleted;

  /**
   * Generate dto from entity
   *
   * @param entity the entity
   * @param roleName the role name
   * @return User
   */
  public static User from(com.exp.test.entity.User entity, String roleName) {
    var user = new User();
    BeanUtils.copyProperties(entity, user);
    // user.setRole(roleName);
    return user;
  }
}
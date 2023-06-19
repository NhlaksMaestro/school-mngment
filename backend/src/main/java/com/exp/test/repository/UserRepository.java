package com.exp.test.repository;

import com.exp.test.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
  /**
   * Find user by email
   *
   * @param email the user's email
   * @return User
   */
  Optional<User> findByEmail(String email);

  /**
   * Find user by student number
   *
   * @param studentNumber the user's student number
   * @return User
   */
  Optional<User> findByStudentNumber(String studentNumber);

  /**
   * Find user by first nname
   *
   * @param firstName the user's first name
   * @return User
   */
  Optional<User> findByFirstName(String firstName);

  /**
   * Find user by last name
   *
   * @param lastName the user's last name
   * @return User
   */
  Optional<User> findByLastName(String lastName);
}

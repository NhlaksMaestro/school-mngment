package com.exp.test.controller;

import com.exp.test.dto.SearchUser;
import com.exp.test.dto.User;
import com.exp.test.dto.Users;
import com.exp.test.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/** User controller. */
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
  /** User service */
  @Autowired private UserService userService;

  /**
   * Get list of user
   *
   * @param query the query filters
   * @return Users
   */
  @GetMapping()
  public Users getUsers(@Valid SearchUser query) {
    return userService.searchUser(query);
  }

  /**
   * Add new user
   *
   * @param request the user detail
   * @return User
   */
  @PostMapping()
  public User addUser(@RequestBody @Valid User request) {
    return userService.addUser(request);
  }

  /**
   * Get user by id
   *
   * @param userId the user id
   * @return User
   */
  @GetMapping("/{userId}")
  public User getUser(@PathVariable("userId") Integer userId) {
    return userService.getUser(userId);
  }

  /**
   * Update user by id
   *
   * @param userId the user id
   * @param request the update request
   * @return User
   */
  @PutMapping("/{userId}")
  public User updateUser(
      @PathVariable("userId") Integer userId, @RequestBody @Valid User request) {
    return userService.updateUser(userId, request);
  }

  /**
   * Delete user by id
   *
   * @param userId the user id
   * @return void
   */
  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}

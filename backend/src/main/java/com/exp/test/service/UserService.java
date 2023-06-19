package com.exp.test.service;

import static java.util.stream.Collectors.toList;

import com.exp.test.dto.SearchUser;
import com.exp.test.dto.User;
import com.exp.test.dto.Users;
import com.exp.test.exception.BadRequestException;
import com.exp.test.exception.NotFoundException;
import com.exp.test.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  /**
   * Search user
   *
   * @param request the query filters
   * @return Users
   */
  public Users searchUser(SearchUser request) {
    var page = PageRequest.of(
        request.getPage(),
        request.getLimit(),
        Sort.Direction.fromOptionalString(request.getSortOrder()).get(),
        request.getSortBy());
    // set query filter
    var q = request.getSearch();
    if (q != null && q.length() > 0) {
      return convert(
          userRepository.findAll(
              equalSpec("firstName", q).or(equalSpec("lastName", q)).or(equalSpec("email", q))
                  .or(equalSpec("studentNumber", q)),
              page));
    }
    var items = userRepository.findAll(page);
    return convert(items);
  }

  /**
   * Generate equal specification
   *
   * @param propertyName the property name
   * @param q            the query value
   * @return Specification
   */
  private Specification<com.exp.test.entity.User> equalSpec(
      String propertyName, String q) {
    return (r, y, b) -> b.equal(r.get(propertyName), q);
  }

  /**
   * Convert user page to users
   *
   * @param userPage the user page
   * @return Users
   */
  private Users convert(Page<com.exp.test.entity.User> userPage) {
    var result = new Users();
    result.setTotal(userPage.getTotalElements());
    result.setItems(
        userPage.get().map(u -> User.from(u, "")).collect(toList()));
    return result;
  }

  /**
   * Add user
   *
   * @param request the add user request
   * @return User
   */
  public User addUser(User request) {
    if (request.getFirstName() == null) {
      throw new BadRequestException("password cannot be null");
    } else if (request.getLastName() == null) {
      throw new BadRequestException("password cannot be null");
    }
    var user = new com.exp.test.entity.User();
    BeanUtils.copyProperties(request, user);
    userRepository.save(user);
    return User.from(user, "");
  }

  /**
   * Get user by id
   *
   * @param userId the user id
   * @return User
   */
  public User getUser(Integer userId) {
    var user = getUserEntity(userId);
    return User.from(user, "");
  }

  /**
   * Update a user by id
   *
   * @param userId  the user id
   * @param request the update request
   * @return User
   */
  public User updateUser(Integer userId, User request) {
    var user = getUserEntity(userId);

  // Apply the updates from the request to the user entity
  user.setFirstName(request.getFirstName());
  user.setLastName(request.getLastName());
  user.setDateOfBirth(request.getDateOfBirth());
  user.setCellphone(request.getCellphone());
  user.setEmail(request.getEmail());
  
  // Save the updated user entity
  user = userRepository.save(user);
    return User.from(user, "");
  }

  /**
   * Get user entity
   *
   * @param userId the user id
   * @return User entity
   */
  private com.exp.test.entity.User getUserEntity(Integer userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("user not found"));
  }

  /**
   * Delete a user by id
   *
   * @param userId the user id
   */
  public void deleteUser(Integer userId) {
    userRepository.deleteById(userId);
  }
}

package com.exp.test.dto;

import java.util.List;
import lombok.Data;

@Data
public class Users {
  private Long total;
  private List<User> items;
}

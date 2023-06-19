package com.exp.test.dto;

import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SearchUser {
  private String search;
  private int page = 0;
  private int limit = 10;

  @Pattern(regexp = "firstName|lastName|email|studentNumber")
  private String sortBy = "firstName";

  @Pattern(regexp = "asc|desc")
  private String sortOrder = "desc";
}

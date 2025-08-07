package com.changolaxtra.tools.annotator.service;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
public class SystemUserService {

  public String getUserName() {
    return ObjectUtils.firstNonNull(System.getenv("USER"), System.getenv("USERNAME"), "Buddy");
  }

}

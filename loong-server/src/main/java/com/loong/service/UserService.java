package com.loong.service;

import com.loong.dto.UserLoginDTO;
import com.loong.entity.User;

public interface UserService {

    User login(UserLoginDTO userLoginDTO);
    
}

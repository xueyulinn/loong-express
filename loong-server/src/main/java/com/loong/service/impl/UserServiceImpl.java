package com.loong.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loong.constant.MessageConstant;
import com.loong.dto.UserLoginDTO;
import com.loong.entity.User;
import com.loong.exception.LoginFailedException;
import com.loong.mapper.UserMapper;
import com.loong.properties.WeChatProperties;
import com.loong.service.UserService;
import com.loong.utils.HttpClientUtil;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {

        if (userLoginDTO == null || userLoginDTO.getCode() == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        String openID = getOpenId(userLoginDTO.getCode());

        User user = userMapper.selectByOpenId(openID);

        // if user does not exist, create a new user
        if (user == null) {
            user = new User();
            user.setOpenid(openID);
            userMapper.insert(user);
        }

        return user;
    }

    public String getOpenId(String code) {
        final String REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";

        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("appid", weChatProperties.getAppid());
        paraMap.put("secret", weChatProperties.getSecret());
        paraMap.put("js_code", code);
        paraMap.put("grant_type", "authorization_code");

        String response = HttpClientUtil.doGet(REQUEST_URL, paraMap);

        JSONObject rs = JSON.parseObject(response);

        return rs.getString("openid");
    }

}

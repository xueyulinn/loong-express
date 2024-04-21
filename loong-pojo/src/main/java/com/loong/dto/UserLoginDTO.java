package com.loong.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Miniapp user login dto
 */
@Data
public class UserLoginDTO implements Serializable {

    private String code;

}

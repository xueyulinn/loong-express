package com.loong.dto;

import java.io.Serializable;

import lombok.Data;


@Data
public class EmployeePasswordDTO implements Serializable{
    private Integer empId;

    private String newPassword;

    private String oldPassword;

}

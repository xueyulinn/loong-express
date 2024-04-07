package com.loong.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeePageQuery implements Serializable{
    private String name;

    private Integer page;

    private Integer pageSize;
}

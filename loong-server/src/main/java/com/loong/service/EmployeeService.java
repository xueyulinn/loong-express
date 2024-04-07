package com.loong.service;

import com.loong.dto.EmployeeDTO;
import com.loong.dto.EmployeeLoginDTO;
import com.loong.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employeeDTO);

}

package com.loong.service;

import com.loong.dto.EmployeeDTO;
import com.loong.dto.EmployeeLoginDTO;
import com.loong.dto.EmployeePageQuery;
import com.loong.entity.Employee;
import com.loong.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * 
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employeeDTO);

    PageResult queryByPage(EmployeePageQuery pageQuery);

    void modifyStatus(Long id, Integer status);

    Employee queryById(Long id);

    void editEmployee(EmployeeDTO employeeDTO);

}

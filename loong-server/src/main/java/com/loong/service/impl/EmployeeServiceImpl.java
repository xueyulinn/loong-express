package com.loong.service.impl;

import com.loong.constant.MessageConstant;
import com.loong.constant.PasswordConstant;
import com.loong.constant.StatusConstant;
import com.loong.context.BaseContext;
import com.loong.dto.EmployeeDTO;
import com.loong.dto.EmployeeLoginDTO;
import com.loong.entity.Employee;
import com.loong.exception.AccountLockedException;
import com.loong.exception.AccountNotFoundException;
import com.loong.exception.BaseException;
import com.loong.exception.PasswordErrorException;
import com.loong.mapper.EmployeeMapper;
import com.loong.service.EmployeeService;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * employee login
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1、select employee by username
        Employee employee = employeeMapper.getByUsername(username);

        // 2、handle exception
        if (employee == null) {
            // no account found
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            // wrong password
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            // account locked
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3、object return
        return employee;
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        employee.setPassword(PasswordConstant.DEFAULT_PASSWORD);
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employee.setStatus(StatusConstant.ENABLE);
        employeeMapper.insertData(employee);

    }
}

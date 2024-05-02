package com.loong.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.loong.constant.MessageConstant;
import com.loong.constant.PasswordConstant;
import com.loong.constant.StatusConstant;
import com.loong.context.BaseContext;
import com.loong.dto.EmployeeDTO;
import com.loong.dto.EmployeeLoginDTO;
import com.loong.dto.EmployeePageQuery;
import com.loong.dto.EmployeePasswordDTO;
import com.loong.entity.Employee;
import com.loong.exception.AccountLockedException;
import com.loong.exception.AccountNotFoundException;
import com.loong.exception.PasswordEditFailedException;
import com.loong.exception.PasswordErrorException;
import com.loong.mapper.EmployeeMapper;
import com.loong.result.PageResult;
import com.loong.service.EmployeeService;

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
        Employee employee = employeeMapper.selectByUsername(username);

        // 2、handle exception
        if (employee == null) {
            // no account found
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //
        password = DigestUtils.md5DigestAsHex(password.getBytes());
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

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        employee.setStatus(StatusConstant.ENABLE);

        employeeMapper.insert(employee);

    }

    /**
     * query employee by page
     *
     * @param pageQuery
     * @return
     */
    @Override
    public PageResult queryByPage(EmployeePageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize());

        Page<Employee> page = employeeMapper.selectLimit(pageQuery);

        List records = page.getResult();
        Long total = page.getTotal();

        return new PageResult(total.intValue(), records);

    }

    @Override
    public void modifyStatus(Long id, Integer status) {
        Employee employee = Employee.builder().id(id).status(status).build();
        employeeMapper.updateStatus(employee);
    }

    @Override
    public Employee queryById(Long id) {

        Employee employee = employeeMapper.selectById(id);

        return employee;
    }

    @Override
    public void editEmployee(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.update(employee);
    }

    @Override
    public void editPassword(EmployeePasswordDTO employeePasswordDTO) {
        Employee employee = new Employee();
        employee.setId(BaseContext.getCurrentId());
        String oldPassword = DigestUtils.md5DigestAsHex(employeePasswordDTO.getOldPassword().getBytes());
        String password = employeeMapper.selectPasswordById(employee.getId());

        if (!password.equals(oldPassword)) {
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }

        employee.setPassword(employeePasswordDTO.getNewPassword());

        employeeMapper.updatePassword(employee);
    }
}

package com.loong.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loong.constant.JwtClaimsConstant;
import com.loong.dto.EmployeeDTO;
import com.loong.dto.EmployeeLoginDTO;
import com.loong.dto.EmployeePageQuery;
import com.loong.entity.Employee;
import com.loong.properties.JwtProperties;
import com.loong.result.PageResult;
import com.loong.result.Result;
import com.loong.service.EmployeeService;
import com.loong.utils.JwtUtil;
import com.loong.vo.EmployeeLoginVO;

import lombok.extern.slf4j.Slf4j;

/**
 * employee management
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * modify employee status
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("status/{status}")
    public Result modifyStatus(@PathVariable Integer status,  Long id) {
        employeeService.modifyStatus(id, status);
        return Result.success();
    }

    /**
     * query employee by page
     *
     * @param pageQuery
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> queryByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {

        EmployeePageQuery pageQuery = EmployeePageQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .build();

        PageResult pageResult = employeeService.queryByPage(pageQuery);

        return Result.success(pageResult);
    }

    /**
     * add employee
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping()
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO) {

        employeeService.addEmployee(employeeDTO);

        return Result.success();
    }

    /**
     * employee login
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("employee login：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        // generate jwt token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * logout
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}

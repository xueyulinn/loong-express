package com.loong.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.loong.dto.EmployeePageQuery;
import com.loong.entity.Employee;

@Mapper
public interface EmployeeMapper {

    /**
     * query employee by page
     * 
     * @param pageQuery
     * @return
     */
    Page<Employee> queryPage(EmployeePageQuery pageQuery);

    /**
     * select employee by username
     * 
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * add employee
     * 
     * @param employeeDTO
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user, status) values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser}, #{status})")
    void insertData(Employee employee);

    @Update("update employee set status = #{status} where id = #{id}")
    void updateStatus(Long id, Integer status);

}

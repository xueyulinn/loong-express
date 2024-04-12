package com.loong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.loong.annotation.AutoFill;
import com.loong.dto.CategoryPageQueryDTO;
import com.loong.entity.Category;
import com.loong.enumeration.OperationType;

@Mapper
public interface CategoryMapper {

    @AutoFill(OperationType.INSERT)
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    List<Category> list(Integer type);

    @Select("select name from category where id = #{categoryId}")
    String selectNameById(Long categoryId);
}

package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from sky_take_out.employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @Insert("insert into sky_take_out.employee(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            "values (#{name}, #{username}, #{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser} )")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    List<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    @Select("SELECT * from sky_take_out.employee where id = #{id}")
    Employee getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);
}

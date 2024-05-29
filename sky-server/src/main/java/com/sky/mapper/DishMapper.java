package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select COUNT(*) from sky_take_out.dish where category_id=#{id}")
    Integer countByCategoryId(Integer id);

    /**
     * 插入菜品数据
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    @Select("select * from sky_take_out.dish")
    List<DishVO> list();

    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据主键查询菜品
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.dish where id=#{id}")
    Dish getById(Long id);

    @Delete("delete from sky_take_out.dish where id=#{id}")
    void deleteById(Long id);
}

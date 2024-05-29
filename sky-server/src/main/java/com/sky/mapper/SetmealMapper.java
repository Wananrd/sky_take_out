package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    @Select("select COUNT(*) from setmeal where category_id=#{id}")
    Integer countByCategoryId(Integer id);
}

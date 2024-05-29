package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    //select setmeal_id from setmeal where dish_id in(1,2,3,4)
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    void insertBatch(List<SetmealDish> setmealDishes);

    @Select("select * from sky_take_out.setmeal_dish where setmeal_id=#{setmeal_id}")
    List<SetmealDish> getBySetmealId(Long setmeal_id);

    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id=#{setmeal_id}")
    void deleteBySetmealId(Long setmeal_id);

    void deleteBySetmealIds(List<Long> ids);
}

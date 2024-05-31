package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //1.设置page, pageSize
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        //2.查找
        List<SetmealVO> setMealList = setmealMapper.pageQuery(setmealPageQueryDTO);
        Page<SetmealVO> p = (Page<SetmealVO>) setMealList;
        //3.返回结果
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 新增套餐
     * @param setMealDTO
     */
    @Override
    @Transactional
    public void add(SetmealDTO setMealDTO) {
        //1.将套餐加入新增菜品表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setMealDTO, setmeal);
        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();

        //2.添加套餐与餐品关联信息
        List<SetmealDish> setmealDishes = setMealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            setMealDishMapper.insertBatch(setmealDishes);
        }

    }


    /**
     * 根据id获取套餐信息
     * @param id
     * @return
     */
    @Override
    public SetmealVO getSetMealVOById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        //1.根据id找到套餐信息
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        //2.根据setmeal_id找到dish
        List<SetmealDish> setmealDishes = setMealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        //1.修改套餐
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        //2.修改套餐菜品关联表
        //删除 套餐菜品关联表 setmeal_id相关数据
        setMealDishMapper.deleteBySetmealId(setmealDTO.getId());

        //添加 套餐菜品关联表 setmeal_id相关数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmeal.getId());
            });
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);

        setmealMapper.update(setmeal);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        log.info("删除套餐:{}", ids);
        //寻找套餐，判断是否有起售的
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //删除套餐
        setmealMapper.deleteByIds(ids);

        //删除套餐关联的菜品表
        setMealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}

package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("添加菜品:{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        //清理缓存
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(){
        log.info("分类id查询菜品");
        List<DishVO> dishVoList = dishService.list();
        return Result.success(dishVoList);
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品:{}", ids);
        dishService.deleteBatch(ids);

        cleanCache("dish_*");

        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品:{}", id);
        DishVO dishVO = dishService.getDishVOById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        cleanCache("dish_*");

        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        dishService.startOrStop(status, id);

        cleanCache("dish_*");

        return Result.success();
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}

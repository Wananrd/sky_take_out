package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 增加分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}", categoryDTO);
        categoryService.insert(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("展示页面:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用/禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result StartOrStop(@PathVariable Integer status, Long id){
        log.info("启用/禁用分类:{},{}", status, id);
        categoryService.StartOrStop(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类:{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(Integer id){
        log.info("根据id删除分类:{}", id);
        categoryService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        log.info("根据类型查询分类:{}", type);

        List<Category> categoryList = categoryService.list(type);
        return Result.success(categoryList);
    }
}
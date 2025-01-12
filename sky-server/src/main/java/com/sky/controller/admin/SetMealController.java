package com.sky.controller.admin;

import com.sky.dto.SetMealDTO;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetMealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     * @param setMealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @Cacheable(value = "setMealCache", key = "#setMealDTO.categoryId")
    public Result save(SetMealDTO setMealDTO) {
        setMealService.saveWithDish(setMealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     * @param setMealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    @Cacheable(value = "setMealCache", key = "#setMealPageQueryDTO.categoryId")
    public Result<PageResult> page(SetMealPageQueryDTO setMealPageQueryDTO) {
        PageResult pageResult = setMealService.pageQuery(setMealPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        setMealService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setMeal = setMealService.getByIdWithDish(id);
        return Result.success(setMeal);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true)
    public Result update(@RequestBody SetMealDTO setMealDTO) {
        setMealService.updateWithDish(setMealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("起售停售套餐")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setMealService.startOrStop(status, id);
        return Result.success();
    }
}

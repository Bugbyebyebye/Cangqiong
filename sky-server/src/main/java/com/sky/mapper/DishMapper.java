package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    Integer insert(Dish dish);

    Integer insertFlavors(List<DishFlavor> flavors);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    // 根据id查询菜品
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteFlavorByDishId(Long id);


    void deleteByIds(List<Long> ids);

    void deleteFlavorByDishIds(List<Long> ids);

    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getFlavorsByDishId(Long id);

    void update(Dish dish);
}

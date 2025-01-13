package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.entity.SetMeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetMealMapper {

    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    @Select("select count(id) from setmeal where category_id = #{id}")
    Integer countByCategoryId(Long id);

    List<SetMeal> list(SetMeal setmeal);

    List<DishItemVO> getDishItemBySetMealId(Long id);

    @Update("update setmeal set status = #{status} where id in #{setMealIds}")
    void updateStatus(Integer disable, List<Long> setMealIds);

    @AutoFill(value = OperationType.INSERT)
    void insert(SetMeal setMeal);

    Page<SetMeal> pageQuery(SetMealPageQueryDTO setMealPageQueryDTO);

    @Delete("delete from setmeal where id = #{id}")
    void deleteByIds(List<Long> ids);

    @Select("select * from setmeal where id = #{id}")
    SetmealVO getByIdWithDish(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(SetMeal setMeal);

    @Select("select * from setmeal where id = #{setmealId}")
    SetMeal getById(Long setmealId);

    Integer countByMap(Map map);
}

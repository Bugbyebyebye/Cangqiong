package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {

    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    @Select("select count(id) from setmeal where category_id = #{id}")
    Integer countByCategoryId(Long id);
}

package com.example.acm.mapper;

import com.example.acm.entity.Label;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author xierenyi
 * @version 1.0
 * @date 2020-02-18 11:34
 */
@Mapper
@Component
public interface LabelMapper {


    /**
     * 添加帖子标签
     *
     * @param label 帖子标签信息
     * 哪怕只有一个参数也得用@Param, 不然#{} 无法访问
     */
    public void addLabel(@Param("label") Label label);

    /**
     * 修改帖子标签
     *
     * @param label 新的帖子标签信息
     */
    public void updateLabel(@Param("label") Label label);

    /**
     * 查询友链
     *
     * @param labelId 帖子标签Id
     * @return 帖子标签列表 以实体类返回
     */
    public List<Label> findLabelListByLabelId(@Param("labelId") Long labelId);

    /**
     * 根据查询条件获取帖子标签信息的个数
     *
     */
    public Integer countLabelListByQuery(@Param("map") Map<String, Object> map);

    /**
     * 根据查询条件获取帖子标签个数(Map)
     *
     */
    public Integer countLabelMapListByQuery(@Param("map") Map<String, Object> map);


    /**
     * 根据查询条件获取帖子标签列表(Map)
     * @Param是mybatis中的注解, 相当于别名, 可以再xml中用@param中的别名来引用参数
     * @return 以map信息返回
     */
    public List<Map<String,Object>> findLabelMapListByQuery(@Param("map") Map<String, Object> map);
}

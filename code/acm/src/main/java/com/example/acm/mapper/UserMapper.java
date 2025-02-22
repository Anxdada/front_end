
package com.example.acm.mapper;

import com.example.acm.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/** 
 * user数据操作接口
 *
 * @author Anxdada
 * @version 1.0
 * @date 2020-02-13 02:40
 */
@Mapper
@Component
public interface UserMapper {
    
    /** 
     * 添加user
     *
     */
    public void addUser(@Param("user") User user);

    /**
     * 更新user
     *
     */
    public void updateUserPC(@Param("user") User user);

    /**
     * 查询user通过Id
     *
     * @param userId 用户id
     * @return 给定userId 查询到的用户, List比较安全
     */
    public List<User> findUserListByUserId(@Param("userId") Long userId);

    /**
     * 根据查询条件获取user个数
     *
     */
    public Integer countUserListByQuery(@Param("map") Map<String, Object> map);
    
    /**
     * 根据查询条件获取user列表
     *
     */
    public List<User> findUserListByQueryMap(@Param("map") Map<String, Object> map);

    /**
     * 根据查询条件获取user列表map
     *
     * @param map 查询条件
     * @return 满足查询条件的所有User
     */
    public Integer countUserMapListByQueryMap(@Param("map") Map<String, Object> map);
    
    /**
     * 根据查询条件获取user列表(Map)
     *
     */
    public List<Map<String,Object>> findUserMapListByQueryMap(@Param("map") Map<String, Object> map);


    /**
     * 找到符合身份的人
     *
     * @param auth 身份
     * @return 人员
     */
    public List<Map<String,Object>> findSatisfyAuthUser(@Param("auth") int auth);
}

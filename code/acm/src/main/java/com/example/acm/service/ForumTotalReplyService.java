package com.example.acm.service;

import com.example.acm.entity.ForumTotalReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (forumTotalReply)表服务接口
 *
 * @author Anxdada
 * @since 2020-02-18 00:00:46
 */
public interface ForumTotalReplyService {

    /**
     * 添加讨论区全部的回复
     *
     * @param forumTotalReply 讨论区全部的回复信息
     * 哪怕只有一个参数也得用@Param, 不然#{} 无法访问
     */
    public void addForumTotalReply(ForumTotalReply forumTotalReply);

    /**
     * 修改讨论区全部的回复
     *
     * @param forumTotalReply 新的讨论区全部的回复信息
     */
    public void updateForumTotalReply(ForumTotalReply forumTotalReply);

    /**
     * 查询友链
     *
     * @param forumTotalReplyId 讨论区全部的回复Id
     * @return 讨论区全部的回复列表 以实体类返回
     */
    public List<ForumTotalReply> findForumTotalReplyListByForumTotalReplyId(Long forumTotalReplyId);

    /**
     * 根据查询条件获取讨论区全部的回复个数(Map)
     * 分页使用
     *
     */
    public Integer countForumTotalReplyList(@Param("map") Map<String, Object> map);


    /**
     * 根据查询条件获取讨论区全部的回复列表(Map)
     * 并且和类别表做连接直接查处结果, 不用二次查询后再返回结果
     * 和上面的函数不同就是字段变少了
     *
     * @return 以map信息返回
     */
    public List<Map<String,Object>> findForumTotalReplyMapListByQuery(Map<String, Object> map);

}
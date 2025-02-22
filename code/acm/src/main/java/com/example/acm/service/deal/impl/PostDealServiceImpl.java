package com.example.acm.service.deal.impl;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.config.RedisComponent;
import com.example.acm.entity.Post;
import com.example.acm.entity.User;
import com.example.acm.service.CommentService;
import com.example.acm.service.LabelService;
import com.example.acm.service.PostService;
import com.example.acm.service.UserService;
import com.example.acm.service.deal.PostDealService;
import com.example.acm.utils.DateUtil;
import com.example.acm.utils.ListPage;
import com.example.acm.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xierenyi
 * @version 1.0
 * @date 2020-02-29 17:36
 */
@Service
public class PostDealServiceImpl implements PostDealService {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private LabelService labelService;

    /**
     * 添加帖子
     * 可能是后台管理员添加的
     * 可能是前台用户添加的
     *
     * @param user 添加的人
     * @param postTag 帖子的标签(二进制)
     * @param postTitle 帖子标题
     * @param postBody 帖子内容
     * @param firstImg 列表展示的小图
     * @return 结果
     */
    public ResultBean addPost(User user, String postTitle, int postTag, String postBody, String firstImg) {
        try {

            Post post = new Post();
            post.setPostTitle(postTitle);
            post.setPostTag(postTag);
            post.setPostBody(postBody);
            post.setCreateUser(user.getUserId());
            post.setCreateTime(new Date());
            post.setUpdateUser(user.getUserId());
            post.setUpdateTime(new Date());
            if (!StringUtil.isNull(firstImg)) post.setFirstImg(firstImg);

            postService.addPost(post);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            // log
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED, String.valueOf(e));
        }
    }

    /**
     * 普通管理员可删除, 用户本身的帖子
     * 管理员删除主要就是因为举报..
     *
     * @param user 删除操作人
     * @param postId 删除的帖子id
     * @return
     */
    public ResultBean deletePost(User user, long postId) {
        try {
            List<Post> list = postService.findPostListByPostId(postId);

            if (list.isEmpty()) return new ResultBean(ResultCode.SQL_NULL_RECODE, "数据库无记录!");

            Post post = list.get(0);
            post.setUpdateUser(user.getUserId());
            post.setUpdateTime(new Date());
            post.setIsEffective(SysConst.NOT_LIVE);

            postService.updatePost(post);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            // log
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED, String.valueOf(e));
        }
    }

    /**
     * 只有帖子的发表人才有权利修改, 管理员都是只能删除, 不能修改其它用户的帖子
     * 管理员修改的是精热置顶等操作
     *
     * @param user 修改的人
     * @param postId 修改的帖子id
     * @param postTag 帖子的标签(二进制)
     * @param postTitle 帖子标题
     * @param postBody 帖子内容
     * @param isHead 置顶
     * @param isGreat 精
     * @param isHot 热
     * @param firstImg 小图
     * @return 结果
     */
    public ResultBean updatePost(User user, long postId, String postTitle, int postTag, String postBody,
                                    int isHead, int isGreat, int isHot, String firstImg) {
        try {
            List<Post> list = postService.findPostListByPostId(postId);

            if (list.isEmpty()) return new ResultBean(ResultCode.SQL_NULL_RECODE, "数据库无记录!");

            Post post = list.get(0);
            if (!StringUtil.isNull(postTitle)) post.setPostTitle(postTitle);
            if (postTag != -1) post.setPostTag(postTag);
            if (!StringUtil.isNull(postBody)) post.setPostBody(postBody);
            if (isHead != -1) post.setIsHead(isHead);
            if (isGreat != -1) post.setIsGreat(isGreat);
            if (isHot != -1) post.setIsHot(isHot);
            post.setUpdateUser(user.getUserId());
            post.setUpdateTime(new Date());
//            System.out.println("xierenyi" + firstImg);
//            System.out.println(StringUtil.isNull(firstImg));
            if (firstImg.equals("undefined") || StringUtil.isNull(firstImg)) {
                post.setFirstImg(null);
            } else {
                post.setFirstImg(firstImg);
            }

            postService.updatePost(post);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            // log
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED, String.valueOf(e));
        }
    }

    /**
     *
     * @param user 当前的登录用户, 用户判定是否可以修改
     * @param postTag 帖子的标签(二进制)
     * @param postTitle 帖子标题
     * @param postStartTime 帖子开始时间
     * @param postEndTime 帖子结束时间
     * @param aOrs 排序规则(升序还是降序)
     * @param order 按照那个字段排序(如果相同, 会按照默认的规则, innodb就是按照)
     * @param pageNum 当前的页数
     * @param pageSize 一页的数量
     * @returnO
     */
    public ResultBean selectPost(User user, String postTitle, int postTag, String postStartTime, String postEndTime,
                                 int aOrs, String order, int pageNum, int pageSize) {
        try {
            Map<String, Object> map = new HashMap<>();
            if (pageNum < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "页码不能小于0");
            }
            if (pageSize < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "一页展示数量不能小于0");
            }
            int start = (pageNum - 1) * pageSize;
            int limit = pageSize;
            map.put("postTitle", postTitle);
            if (postTag != -1) map.put("postTag", postTag);
            if (!StringUtil.isNull(postStartTime)) map.put("postStartTime", postStartTime);
            if (!StringUtil.isNull(postEndTime)) map.put("postEndTime", postEndTime);
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            map.put("isEffective", SysConst.LIVE);

            List<Map<String, Object>> list = postService.findPostMapListByQuery(map);

            if (!list.isEmpty()) {
                for (Map<String, Object> mapTemp : list) {

                    // 查评论数(不包括后面回复评论的, 只在手机端展示列表时需要这个数据)
                    Map<String, Object> mapComment = new HashMap<>();
                    mapComment.put("replyPostId", mapTemp.get("postId"));
                    mapTemp.put("totComment", commentService.findCommentMapListByQuery(mapComment).size());

                    mapTemp.put("isSame", user.getUserId() == (Long)mapTemp.get("createUser"));
                    // 这个是用于判断当前这个帖子是不是登录管理员写的, 如果是那么他就可以修改他的帖子
                    List<User> listUsers = userService.findUserListByUserId((Long)mapTemp.get("createUser"));
//                    System.out.println((Long)mapTemp.get("createUser"));
                    User tUs = null;
                    if (!listUsers.isEmpty()) tUs = listUsers.get(0);
                    if (tUs != null) {
                        mapTemp.put("createRealName", tUs.getRealName());  // 后端需要真实姓名
                        mapTemp.put("createUserName", tUs.getUserName());  // 手机端需要用户名
                        mapTemp.put("avatar", tUs.getAvatar());
                    }
                    mapTemp.put("createTime", DateUtil.convDateToStr((Date) mapTemp.get("createTime"), "yyyy-MM-dd HH:mm:ss"));
                }
            }

            int allNum = postService.countPostList(map);

            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, list);

            return new ResultBean(ResultCode.SUCCESS, listPage);

        } catch (Exception e) {
            // log
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED, String.valueOf(e));
        }
    }


    /**
     * 根据帖子id获取帖子信息
     * 这个是为了解决一个bug, 删除表格一个元素后, 实际的记录还在, 当点修改时存来的记录就是已经删除的了
     * 所以修改需要通过id重新读取信息
     *
     * @param user 当前的登录用户, 用户判定是否可以修改
     * @param postId 帖子Id
     * @return
     */
    public ResultBean detailPost(User user, long postId) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("postId", postId);
            map.put("isEffective", SysConst.LIVE);

            List<Map<String, Object>> list = postService.findPostMapListByQuery(map);

            if (list.isEmpty()) {
                return new ResultBean(ResultCode.SYSTEM_FAILED, "数据库无此记录");
            }

            Map<String, Object> mapTemp = list.get(0);

            // 移动端获取label数组. 只能在后端做, 手机端因为异步以及布局问题没法实施
            int postTag = (int)mapTemp.get("postTag");
            Map<String, Object> mapTag = new HashMap<>();
            mapTag.put("start", 0);
            mapTag.put("limit", 100);
            mapTag.put("isEffective", SysConst.LIVE);
            List<Map<String, Object>> listAllLabels = labelService.findLabelMapListByQuery(mapTag);

            List<Long> postLabels = new ArrayList<>();
            for (int i = 0 ; i < listAllLabels.size() ; ++ i) {
                long flag = (Long)listAllLabels.get(i).get("flag");
                int tmp = postTag & (1 << flag);
                if (tmp != 0) {
                    postLabels.add(flag);
                }
            }

            mapTemp.put("postLabel", postLabels);

//            mapTemp.put("postLabel", )

            // 判定修改删除(移动端上需要)
            // 可能上线后需要改变一下取登录用户的id的方式.. 先标记
            mapTemp.put("isSame", user.getUserId() == (Long)mapTemp.get("createUser"));

            List<User> listUsers = userService.findUserListByUserId((Long)mapTemp.get("updateUser"));
            User tUs = null;
            if (listUsers.size() > 0) tUs = listUsers.get(0);
            if (tUs != null) {
                mapTemp.put("updateRealName", tUs.getRealName());  // 后端需要真实姓名
                mapTemp.put("updateUserName", tUs.getUserName());  // 手机端需要用户名
            }


            listUsers = userService.findUserListByUserId((Long)mapTemp.get("createUser"));
            tUs = null;
            if (listUsers.size() > 0) tUs = listUsers.get(0);
            if (tUs != null) if (tUs != null) {
                mapTemp.put("createRealName", tUs.getRealName());  // 后端需要真实姓名
                mapTemp.put("createUserName", tUs.getUserName());  // 手机端需要用户名
                mapTemp.put("avatar", tUs.getAvatar());
            }

            mapTemp.put("createTime", DateUtil.convDateToStr((Date) mapTemp.get("createTime"), "yyyy-MM-dd HH:mm:ss"));
            mapTemp.put("updateTime", DateUtil.convDateToStr((Date) mapTemp.get("updateTime"), "yyyy-MM-dd HH:mm:ss"));

            // 点赞的
            String key = "post" + mapTemp.get("postId");
            mapTemp.put("isNowUserLikeThisPost", redisComponent.hasMemberForKey(key, String.valueOf(user.getUserId())));
//            mapTemp.put("like", redisComponent.getSizeSetForKey(key)); // 帖子表中有这个字段(因为需要点赞的排序, 和comment有点像) 所以不需要获取


            return new ResultBean(ResultCode.SUCCESS, mapTemp);
        } catch (Exception e) {
            // log
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED, String.valueOf(e));
        }
    }
}

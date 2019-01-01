package com.qf.sc.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.sc.cms.entity.TbContent;
import com.qf.sc.cms.entity.TbContentExample;
import com.qf.sc.cms.mapper.TbContentMapper;
import com.qf.sc.cms.service.ContentService;
import com.qf.sc.common.AppException;
import com.qf.sc.result.AppResult;
import com.qf.sc.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public AppResult findAllContents(Integer pageno, Integer size) {
        PageHelper.startPage(pageno,size);
        List<TbContent> list = contentMapper.selectByExample(null);
        PageInfo<TbContent> model = new PageInfo<>(list);
        PageResult<TbContent> page = new PageResult<>(model.getTotal(),model.getList());
        return new AppResult(true,null,200,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public AppResult findContentById(Long id) {
        TbContent content = contentMapper.selectByPrimaryKey(id);
        return new AppResult(true,null,200,content);
    }


    @Override
    public AppResult updContent(TbContent content) {
        int count = contentMapper.updateByPrimaryKeySelective(content);
        if(count<1){
            throw new AppException("更新失败",201);
        }
        //如果添加成功 清空缓存
        HashOperations hops = redisTemplate.opsForHash();
        hops.delete("portal_contents",content.getCategoryId()+"");
        return new AppResult(true,"更新广告成功",200,content);
    }

    @Override
    public AppResult addContent(TbContent content) {
        int count = contentMapper.insert(content);
        if(count<1){
            throw new AppException("添加失败",201);
        }
        //如果添加成功 清空缓存
        HashOperations hops = redisTemplate.opsForHash();
        hops.delete("portal_contents",content.getCategoryId()+"");
        return new AppResult(true,"添加广告成功",200,content);
    }

    @Override
    public AppResult delContentBatch(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            TbContent content = contentMapper.selectByPrimaryKey(id);
            int count = contentMapper.deleteByPrimaryKey(id);
            if (count<1){
                throw new AppException("删除品牌失败id="+id,201);
            }
            HashOperations hops = redisTemplate.opsForHash();
            hops.delete("portal_contents",content.getCategoryId()+"");
        }
        return new AppResult(true,"删除广告成功",200,null);
    }

    @Override
    public AppResult findContentByCatId(Long id) {
        //先去redis中去查找
        HashOperations hops=redisTemplate.opsForHash();
        List<TbContent> list = (List<TbContent>) hops.get("portal_contents",id+"");
        //如果redis中没有，那么久去数据库中去查找
        if(list==null){
            TbContentExample example = new TbContentExample();
            example.createCriteria().andCategoryIdEqualTo(id).andStatusEqualTo("1");
            //根据升序或者降序排序
            example.setOrderByClause("sort_order desc");
            list  = contentMapper.selectByExample(example);
            //查询完成之后再redis中保存一份
            hops.put("portal_contents",id+"",list);
        }
        return new AppResult(true,"success",200,list);
    }
}

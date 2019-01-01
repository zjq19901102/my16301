package com.qf.sc.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.sc.cms.entity.TbContentCategory;
import com.qf.sc.cms.entity.TbContentCategoryExample;
import com.qf.sc.cms.mapper.TbContentCategoryMapper;
import com.qf.sc.cms.service.ContentCategoryService;
import com.qf.sc.common.AppException;
import com.qf.sc.result.AppResult;
import com.qf.sc.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public AppResult findAllCats() {
        return new AppResult(true,null,200,contentCategoryMapper.selectByExample(null));
    }

    @Override
    public AppResult addContentCat(TbContentCategory contentCategory) {
        int  count = contentCategoryMapper.insert(contentCategory);
        if(count<1){
            throw new AppException("添加失败",201);
        }
        return new AppResult(true,"添加成功",200,contentCategory);
    }

    @Override
    public AppResult delContentCat(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            int count = contentCategoryMapper.deleteByPrimaryKey(id);
            if (count<1){
                throw new AppException("删除广告类型失败id="+id,201);
            }
        }
        return new AppResult(true,"删除广告类型成功",200,null);
    }

    @Override
    public AppResult updContentCat(TbContentCategory contentCategory) {
        int  count = contentCategoryMapper.updateByPrimaryKey(contentCategory);
        if(count<1){
            throw new AppException("更新失败",201);
        }
        return new AppResult(true,"更新成功",200,contentCategory);
    }

    @Override
    public AppResult findConCatById(Long id) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if(contentCategory==null){
            throw new AppException("没有此类广告",201);
        }
        return new AppResult(true,"查询成功",200,contentCategory);
    }

    @Override
    public AppResult findConCatByInput(String name, Integer pageno, Integer size) {
        PageHelper.startPage(pageno,size);
        TbContentCategoryExample example = new TbContentCategoryExample();
        PageResult<TbContentCategory> result =null;
        if(StringUtils.isEmpty(name)==false){
            example.createCriteria().andNameLike("%"+name+"%");
        }
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        PageInfo<TbContentCategory> info = new PageInfo<>(list);
        result = new PageResult<>(info.getTotal(),info.getList());
        return new AppResult(true,"查询成功",200,result);
    }
}

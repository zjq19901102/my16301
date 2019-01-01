package com.qf.sc.cms.service;

import com.qf.sc.cms.entity.TbContentCategory;
import com.qf.sc.result.AppResult;

public interface ContentCategoryService {
    //查询所有的广告的种类
    AppResult findAllCats();
    //添加广告
    AppResult addContentCat(TbContentCategory contentCategory);
    //删除广告
    AppResult delContentCat(Long[] ids);
    //更新广告
    AppResult updContentCat(TbContentCategory contentCategory);
    //根据id查询广告
    AppResult findConCatById(Long id);
    //根据页面的输入查询广告的类型
    AppResult findConCatByInput(String name,Integer pageno ,Integer size);
}

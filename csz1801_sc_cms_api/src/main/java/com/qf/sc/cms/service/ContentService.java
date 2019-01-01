package com.qf.sc.cms.service;

import com.qf.sc.cms.entity.TbContent;
import com.qf.sc.result.AppResult;

public interface ContentService {
    //查询所有的广告
    AppResult findAllContents(Integer pageno,Integer size);
    AppResult findContentById(Long id);
    AppResult updContent(TbContent content);
    AppResult addContent(TbContent content);
    AppResult delContentBatch(Long[] ids);
    //根据广告种类id查询所有的广告
    AppResult findContentByCatId(Long id);
}

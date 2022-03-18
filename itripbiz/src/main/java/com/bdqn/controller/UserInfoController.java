package com.bdqn.controller;


import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.itrip.pojo.*;
import itrip.common.Dto;
import itrip.common.DtoUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller

public class UserInfoController {
    @Resource
    ItripAreaDicMapper dao;

    @Resource
    ItripUserLinkUserMapper dao2;

    @RequestMapping(value = "/api/userinfo/modifyuserlinkuser",produces = "application/json;charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public Dto modifyuserlinkuser(@RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO) throws Exception {
        ItripUserLinkUser i=new ItripUserLinkUser();
        i.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
        i.setLinkUserName(itripModifyUserLinkUserVO.getLinkUserName());
        i.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
        i.setId(itripModifyUserLinkUserVO.getId());
        int num=dao2.updateItripUserLinkUser(i);


        if (num>0) {
            return DtoUtil.returnDataSuccess("修改成功");
        } else {
            return DtoUtil.returnFail("修改失败", "10000");
        }
    }

    @RequestMapping(value = "/api/userinfo/deluserlinkuser",produces = "application/json;charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public Dto deluserlinkuser(Integer[] ids) throws Exception {
        int num=dao2.deleteItripUserLinkUserById(ids);
        if (num>0) {
            return DtoUtil.returnDataSuccess("删除成功");
        } else {
            return DtoUtil.returnFail("删除失败", "10000");
        }
    }


    @RequestMapping(value = "/api/userinfo/adduserlinkuser",produces = "application/json;charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public Dto adduserLinkuser(@RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO, HttpServletRequest request) throws Exception {
        ItripUserLinkUser i=new ItripUserLinkUser();
        i.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
        i.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
        i.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
        int num=dao2.insertItripUserLinkUser(i);

        if (num>0) {
            return DtoUtil.returnDataSuccess("新增成功");
        } else {
            return DtoUtil.returnFail("新增失败", "10000");
        }
    }

   @RequestMapping(value = "/api/userinfo/queryuserlinkuser",produces = "application/json;charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public Dto queryhotcityByType(@RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO, HttpServletRequest request) throws Exception {
        String tokenString=request.getHeader("token");
        String linkUserName=itripSearchUserLinkUserVO.getLinkUserName();
        Map param=new HashMap();
        param.put("linkUserName",linkUserName);
        List<ItripUserLinkUser> list=dao2.getItripUserLinkUserListByMap(param);
        if(list!=null){
            return DtoUtil.returnDataSuccess(list);
        }else{
            return DtoUtil.returnFail("查询失败","10000");
        }

   }



}

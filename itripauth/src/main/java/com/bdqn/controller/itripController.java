package com.bdqn.controller;


import cn.itrip.dao.itripHotel.ItripHotelMapper;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripHotel;
import cn.itrip.pojo.ItripUser;
import cn.itrip.pojo.ItripUserVO;
import com.alibaba.fastjson.JSONArray;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import itrip.common.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import java.util.*;


@Controller
public class itripController {

    @Resource
    ItripHotelMapper dao;

    @Resource
    TokenBiz biz;

    @Resource
    RedisUtil RedisUtil;

    @Resource
    ItripUserMapper dao1;

    @Resource
    ItripUserMapper dao2;

    @RequestMapping(value="api/ckusr",produces="application/json;charset=utf-8")

    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    public Dto Registeremill() throws Exception {
        return DtoUtil.returnSuccess();
    }

    @RequestMapping(value="api/doregister",produces="application/json;charset=utf-8",method = RequestMethod.POST)
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto Registeremil(@RequestBody ItripUserVO vo) throws Exception {
        //把前台信息插入数据库中
        ItripUser user=new ItripUser();
        user.setUserCode(vo.getUserCode());
        user.setUserPassword(vo.getUserPassword());
        user.setUserName(vo.getUserName());
        dao2.insertItripUser(user);
        //发送短信验证码
        Random random=new Random(4);
        int sj=random.nextInt(9999);
        Client.sentSmail(user.getUserCode(),""+sj);
        RedisUtil.setRedis(vo.getUserCode(),""+sj);
        return DtoUtil.returnSuccess();

    }

    @RequestMapping(value="api/activate",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto rupdateemil(String user,String code) throws Exception {
        String code1=RedisUtil.getstr(user);
        if (code1.equals(code)){
            dao2.kkk(user);
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("激活失败","10000");
    }


    @RequestMapping(value="api/registerbyphone",produces="application/json;charset=utf-8",method = RequestMethod.GET)
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto Register(@RequestBody ItripUserVO vo) throws Exception {
        //把前台信息插入数据库中
        ItripUser user=new ItripUser();
        user.setUserCode(vo.getUserCode());
        user.setUserPassword(vo.getUserPassword());
        user.setUserName(vo.getUserName());
        dao2.insertItripUser(user);
        //发送短信验证码
        Random random=new Random(4);
        int sj=random.nextInt(9999);
        sentSms(vo.getUserCode(),""+sj);
        RedisUtil.setRedis(vo.getUserCode(),""+sj);
        return DtoUtil.returnSuccess();

    }


    @RequestMapping(value="/api/validatephone",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto rupdate(String user,String code) throws Exception {
      String code1=RedisUtil.getstr(user);
      if (code1.equals(code)){
          dao2.kkk(user);
          return DtoUtil.returnSuccess();
      }
      return DtoUtil.returnFail("激活失败","10000");
    }




    @RequestMapping(value="/api/dologin",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto login(String name, String password,HttpServletRequest request) throws Exception {
        Map b=new HashMap<>();
        b.put("a",name);
        b.put("b",password);
        ItripUser user=dao1.getItripUserListByMap(b);
        //模拟session的票据
        if(user!=null) {
            String token = biz.generateToken(request.getHeader("User-Agent"), user);
            //把这个token存贮到redis中
            //fastjson把当前用户转换成字符串
            RedisUtil.setRedis(token,JSONArray.toJSONString(user));
            ItripTokenVO obj=new ItripTokenVO(token, Calendar.getInstance().getTimeInMillis()*7200,Calendar.getInstance().getTimeInMillis());
            return DtoUtil.returnDataSuccess(obj);
        }

        //通过fastjson把当前对象转换成字符串
        //return JSONArray.toJSONString(user);
        return DtoUtil.returnFail("登录失败","1000");
    }

    public static void sentSms(String phone,String message){
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8a216da87f63aaf1017f6c3ae9c501eb";
        String accountToken = "c7c4fa5aac34432b928398f4d5b331a3";
        //请使用管理控制台中已创建应用的APPID
        String appId = "8a216da87f63aaf1017f6c3aeac301f1";
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String to = "17778159444";
        String templateId= "1";
        String[] datas = {message};
        String subAppend="1234";  //可选	扩展码，四位数字 0~9999
        String reqId="fadfafas";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
        //HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas,subAppend,reqId);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }




    @RequestMapping(value="clist1",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public String glist(String pid) throws Exception {
        ItripHotel list = dao.getItripHotelById(new Long(56));

        //通过fastjson把当前对象转换成字符串
        return JSONArray.toJSONString(list);
    }

    @RequestMapping("/clist")
    public String clist(){

        return "clist";
    }

}
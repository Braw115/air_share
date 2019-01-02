package com.air.constant;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.air.pojo.entity.AppUser;
import com.air.redis.RedisAPI;

public class ValidationToken {


    private Logger logger = Logger.getLogger(ValidationToken.class);

    private RedisAPI redisAPI;

    public RedisAPI getRedisAPI() {
        return redisAPI;
    }
    public void setRedisAPI(RedisAPI redisAPI) {
        this.redisAPI = redisAPI;
    }
    public AppUser getCurrentUser(String tokenString){
        //根据token从redis中获取用户信息
   /*
    test token:
    key : token:1qaz2wsx
    value : {"id":"100078","userCode":"myusercode","userPassword":"78ujsdlkfjoiiewe98r3ejrf","userType":"1","flatID":"10008989"}

   */
    	AppUser itripUser = null;
        if(null == tokenString || "".equals(tokenString)){
            return null;
        }
        try{
            String userInfoJson = redisAPI.get(tokenString);
            itripUser = JSONObject.parseObject(userInfoJson,AppUser.class);
            
            System.out.println("根据token获取redis中的用户信息：" + itripUser);
        }catch(Exception e){
            itripUser = null;
            logger.error("get userinfo from redis but is error : " + e.getMessage());
        }
        return itripUser;
    }
}
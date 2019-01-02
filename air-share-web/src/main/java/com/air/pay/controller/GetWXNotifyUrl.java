package com.air.pay.controller;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.pojo.entity.Order;
import com.air.user.service.OrderService;
import com.air.utils.EmptyUtils;
import com.air.utils.WeiChartUtil;
import com.air.utils.XMLUtil4jdom;

@Controller
public class GetWXNotifyUrl {

    protected static final Logger LOG = LoggerFactory
            .getLogger(GetWXNotifyUrl.class);
    @Autowired
    private OrderService orderService;

    
    @RequestMapping(value = "payNotifyUrl", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String payNotifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader reader = null;

        reader = request.getReader();
        String line = "";
        String xmlString = null;
        StringBuffer inputString = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        xmlString = inputString.toString();
        request.getReader().close();
        System.out.println("----接收到的数据如下：---" + xmlString);
        Map<String, String> map = new HashMap<String, String>();
        String result_code = "";
        String return_code = "";
        String out_trade_no = "";
        map = XMLUtil4jdom.doXMLParse(xmlString);
        result_code = map.get("result_code");
        out_trade_no = map.get("out_trade_no");
        return_code = map.get("return_code");

        if (checkSign(xmlString)) {
        	Order order = orderService.queryOrderByNo(out_trade_no);
        	order.setPaymethod("微信");
			this.orderService.modifyOrderInfo(order);
            return returnXML(result_code);
        } else {
            return returnXML("FAIL");
        }


    }

    private boolean checkSign(String xmlString) {

        Map<String, String> map = null;

        try {

            map = XMLUtil4jdom.doXMLParse(xmlString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String signFromAPIResponse = map.get("sign").toString();

        if (signFromAPIResponse == "" || signFromAPIResponse == null) {

            System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");

            return false;

        }
        System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);

        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名

        map.put("sign", "");

        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较

        String signForAPIResponse = WeiChartUtil.getSign(map);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {

            //签名验不过，表示这个API返回的数据有可能已经被篡改了

            System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! signForAPIResponse生成的签名为" + signForAPIResponse);

            return false;

        }

        System.out.println("恭喜，API返回的数据签名验证通过!!!");

        return true;

    }
    private String returnXML(String return_code) {

        return "<xml><return_code><![CDATA["

                + return_code

                + "]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
package com.air.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/unchecked")
public class BaseController {

	public void outPrint(HttpServletResponse response, String result)  throws IOException {
		PrintWriter out = response.getWriter();
		out.print(result);
	}

	protected Map<String, Object> success(Object data) {
		return toMap("data", data, "result","");
	}

	protected Map<String, Object> error(Object data) {
		return toMap("data", data, "result", "");
	}

	protected Map<String, Object> error(Throwable t) {
		return toMap("data", t.getMessage(), "result", "");
	}

	public static Map toMap(Object... params) {
		Map map = new LinkedHashMap();
		Assert.notNull(params);
		Assert.isTrue(params.length % 2 == 0);
		for (int i = 0; i < params.length; i++) {
			map.put(params[i++], params[i]);
		}
		return map;
	}
}

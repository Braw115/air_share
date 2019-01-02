package com.air.pay.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.air.pojo.entity.RedBoxRate;

public class RedBoxUtil {
	
	private static Random random = new Random();
	
	public static BigDecimal getValue(List<RedBoxRate> list) {
			Integer key = random.nextInt(100);
			Integer rate = 0;
			list.get(list.size()-1).setRedBoxMax(list.get(list.size()-1).getRedBoxMax().add(new BigDecimal("1")));
			for (int i = 0; i < list.size(); i++) {
				
				if (list.get(i).getRedBoxMax().intValue()==0&&key>=0 && key<(list.get(i).getRedBoxRate()+0)) {
					return new BigDecimal(0);
				}else if(key>=rate && key<(list.get(i).getRedBoxRate()+rate)) {
					return new BigDecimal(random.nextInt(list.get(i).getRedBoxMax().intValue()-list.get(i).getRedBoxMin().intValue())+list.get(i).getRedBoxMin().intValue());
				}
				rate+=list.get(i).getRedBoxRate();
				if (rate>=100) {
					break;
				}
			}
			return new BigDecimal(0);
		}
}


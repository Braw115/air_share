package com.air.utils;

import java.util.Random;

public class PwdUtil {
	
	private static Random rand;
	static{
		rand=new Random();
	}
	
	public static int getNum(){
		return getRadomInt(0, 9);
	}

	/**
	 * 获取一个范围内的随机数字
	 * @return
	 */
	public static int getRadomInt(int min,int max){
		return rand.nextInt(max-min+1)+min;
	}
	public static String getPasswords(Integer wordNum){
		int total=wordNum;//密码总位数
		
		StringBuffer sb=new StringBuffer();
		//组装密码
		for(int i=0;i<total;i++){
			sb.append(getNum());
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getPasswords(6));
	}
	
}

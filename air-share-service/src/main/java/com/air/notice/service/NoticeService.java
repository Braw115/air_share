package com.air.notice.service;

import java.util.List;

import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.Notice;
import com.github.pagehelper.PageInfo;

public interface NoticeService {
	
	/**
	 * 
	 * <p>Title: selectNoticeList</p>
	 * <p>Description: 获取通知列表</p>
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageInfo<Notice> selectNoticeList(Integer pageNum, Integer pageSize,Integer appUserId);

	/**
	 * 添加消息
	 * @param notice
	 * @return
	 */
	Boolean addNotice(Notice notice);


}

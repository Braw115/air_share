package com.air.notice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.notice.mapper.NoticeMapper;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.Notice;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private NoticeMapper noticeMapper;

	@Override
	public PageInfo<Notice> selectNoticeList(Integer pageNum, Integer pageSize,Integer appUserId) {
		PageHelper.startPage(pageNum, pageSize);
		List<Notice> list = noticeMapper.selectNoticeList(appUserId);
		PageInfo<Notice> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public Boolean addNotice(Notice notice) {
		
		return noticeMapper.insertNotice(notice);
	}
	
}

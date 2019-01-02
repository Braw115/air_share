package com.air.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.Notice;

public interface NoticeMapper {
	/**
	 * 
	 * <p>Title: selectNoticeList</p>
	 * <p>Description: 通知列表 </p>
	 * @return
	 */
	List<Notice> selectNoticeList(Integer appUserId);

	/**
	 * 
	 * <p>Title: insertNotice</p>
	 * <p>Description: 添加通知信息 </p>
	 * @return
	 */
	Boolean insertNotice(Notice notice);
	
	/**
	 * 
	 * <p>Title: selectNoticeList</p>
	 * <p>Description: 通知列表 </p>
	 * @return
	 */
	List<Notice> selectNoticeListBySend(boolean send);
	
	/**
	 * 
	 * <p>Title: updateNoticeByIdList</p>
	 * <p>Description: 更新发送状态 </p>
	 * @return
	 */
	Boolean updateNoticeByIdList(@Param("noticeIdList")List<Integer> noticeIdList);

	/**
	 * 
	 * <p>Title: insertNoticeList</p>
	 * <p>Description: 批量添加通知信息 </p>
	 * @return
	 */
	Boolean insertNoticeList(@Param("notices")List<Notice> notices);
}

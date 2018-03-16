package com.lookup.dynamic.mutiple.dao;

import org.apache.commons.dbutils.handlers.BeanHandler;

import com.lookup.dynamic.mutiple.util.DBUtil;
import com.lookup.dynamic.mutiple.vo.StackOverFlowVO;

/**
 * @author 作者:sekift 
 * @author E-mail:sekiftlyz@gmail.com
 * @version 创建时间：2015-10-24 上午02:11:25
 * 类说明:[]
 */
public class StackOverFlowDao {
	/**
	 * 插入或更新数据
	 * @param args
	 */
	public boolean save(StackOverFlowVO vo){
		StringBuilder sql=new StringBuilder();
		sql.append("INSERT INTO stackoverflow(id,title,questionContent,questionVote,answerFirstContent," +
				"answerFirstVote,answerSecondContent,answerSecondVote,tag,createTime,questionTime,status,more,website)" +
				" VALUES(?,?,?,?,?,?,?,?,?,now(),?,1,?,'www.stackoverflow.com/questions/')");
		sql.append(" on duplicate key update title=?,questionContent=?,questionVote=?,answerFirstContent=?");
		sql.append(",answerFirstVote=?,answerSecondContent=?,answerSecondVote=?,tag=?,createTime=now()");
		boolean result=DBUtil.updateQuietly(sql.toString(), 
				vo.getId(),
				vo.getTitle(),
				vo.getQuestionContent(),
				vo.getQuetionVote(),
				vo.getAnswerFirstContent(),
				vo.getAnswerFirstVote(),
				vo.getAnswerSecondContent(),
				vo.getAnswerSecondVote(),
				vo.getTag(),
				vo.getQuestionTime(),
				vo.getMore(),
				
				vo.getTitle(),
				vo.getQuestionContent(),
				vo.getQuetionVote(),
				vo.getAnswerFirstContent(),
				vo.getAnswerFirstVote(),
				vo.getAnswerSecondContent(),
				vo.getAnswerSecondVote(),
				vo.getTag()
		        )>0;
		return result;
	}
	
	/**
	 * 删除或恢复数据
	 * @param args
	 */
	public boolean deleteOrRecovery(int id, boolean dr) {
		String sql = null;
		if (dr) {
			sql = "UPDATE stackoverflow SET status=1 WHERE id=?";
		} else {
			sql = "UPDATE stackoverflow SET status=0 WHERE id=?";
		}
		return DBUtil.updateQuietly(sql, id) > 0;
	}
    
	/**
	 * 真正删除数据
	 * @param args
	 */
	public boolean deleteTrue(int id) {
		String sql = "DELETE FROM stackoverflow WHERE id=?";
		return DBUtil.updateQuietly(sql, id) > 0;
	}
	
	/**
	 * 查询一条数据
	 * @param args
	 */
	public StackOverFlowVO query(int id) {
		StackOverFlowVO vo = null;
		String sql = "SELECT * FROM stackoverflow WHERE id=?";
		vo = (StackOverFlowVO) DBUtil.queryQuietly(sql,
				new BeanHandler<StackOverFlowVO>(StackOverFlowVO.class), id);
		return vo;
	}
	
}

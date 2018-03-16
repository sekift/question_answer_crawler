package com.lookup.dynamic.mutiple.vo;

import java.util.Date;

/**
 * @author 作者:sekift 
 * @author E-mail:sekiftlyz@gmail.com
 * @version 创建时间：2015-10-24 上午01:32:44
 * 类说明:[]
 */
public class StackOverFlowVO {
    private int id;//`id` int(10) unsigned zerofill NOT NULL COMMENT '问题序号',
    private String title;//`title` varchar(128) DEFAULT NULL COMMENT '题目',
    private String questionContent;//`questionContent` varchar(2560) DEFAULT NULL COMMENT '问题内容',
    private int quetionVote;//`questionVote` int(10) DEFAULT NULL COMMENT '问题投票数',
    private String answerFirstContent;//`answerFirstContent` varchar(2560) DEFAULT NULL COMMENT '第一条回答内容',
    private int answerFirstVote;// `answerFirstVote` int(10) DEFAULT NULL COMMENT '第一条回答投票',
    private String answerSecondContent;//`answerSecondContent` varchar(2560) DEFAULT NULL COMMENT '第二条回答内容',
    private int answerSecondVote;//`answerSecondVote` int(10) DEFAULT NULL COMMENT '第二条回答投票',
	private String tag;//`tag` varchar(50) DEFAULT NULL COMMENT '标签',
	private Date createTime; //插入时间
	private String questionTime;//`questionTime` datetime DEFAULT NULL COMMENT '提问时间',
    private int status;//`status` tinyint(4) DEFAULT NULL COMMENT '状态；1 使用，0 无用',
    private String more;//`more` varchar(128) DEFAULT NULL COMMENT '备用字段',
    private String website;//`website` varchar(50) DEFAULT 'www.stackoverflow.com/questions/' COMMENT 'website',
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQuestionContent() {
		return questionContent;
	}
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	public int getQuetionVote() {
		return quetionVote;
	}
	public void setQuetionVote(int quetionVote) {
		this.quetionVote = quetionVote;
	}
	public String getAnswerFirstContent() {
		return answerFirstContent;
	}
	public void setAnswerFirstContent(String answerFirstContent) {
		this.answerFirstContent = answerFirstContent;
	}
	public int getAnswerFirstVote() {
		return answerFirstVote;
	}
	public void setAnswerFirstVote(int answerFirstVote) {
		this.answerFirstVote = answerFirstVote;
	}
	public String getAnswerSecondContent() {
		return answerSecondContent;
	}
	public void setAnswerSecondContent(String answerSecondContent) {
		this.answerSecondContent = answerSecondContent;
	}
	public int getAnswerSecondVote() {
		return answerSecondVote;
	}
	public void setAnswerSecondVote(int answerSecondVote) {
		this.answerSecondVote = answerSecondVote;
	}
    public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getQuestionTime() {
		return questionTime;
	}
	public void setQuestionTime(String questionTime) {
		this.questionTime = questionTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMore() {
		return more;
	}
	public void setMore(String more) {
		this.more = more;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
}

/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.parser.imp;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

import com.lookup.dynamic.actor.base.BaseActor;
import com.lookup.dynamic.mutiple.dao.StackOverFlowDao;
import com.lookup.dynamic.mutiple.vo.StackOverFlowVO;
import com.lookup.dynamic.response.TaskResponse;

/**
 * Created by Administrator on 2015/8/15 0015.
 */

public class StackOverFlowParser extends BaseActor {
	private Logger logger = LoggerFactory.getLogger("deadletter");

	@Inject
    private StackOverFlowDao stackOverFlowDao;
	
	@Override
	public void parallelProcess(ActorRef sender, Object message,
			ActorRef recipient, ActorContext context) {
		if (message instanceof TaskResponse) {
			TaskResponse response = (TaskResponse) message;
			String html = response.getResponseMeta().getBody();
			Document doc = Jsoup.parse(html);
			StackOverFlowVO vo = new StackOverFlowVO();
			try {
				//id
				String idStr = doc.getElementsByClass("question-hyperlink")
						.get(0).attributes().get("href");
				String[] list = idStr.split("/");
				vo.setId(Integer.valueOf(list[2]));
				// title
				String title = doc.getElementsByClass("question-hyperlink")
						.get(0).text();
				vo.setTitle(title);
				// questionContent answerContent
				Elements textEles = doc.getElementsByClass("post-text");
				Elements voteEles = doc.getElementsByClass("vote-count-post");

				if (textEles.size() > 2) {
					vo.setQuestionContent(textEles.get(0).toString());
					vo.setQuetionVote(Integer.valueOf(voteEles.get(0).text()));
					vo.setAnswerFirstContent(textEles.get(1).toString());
					vo.setAnswerFirstVote(Integer.valueOf(voteEles.get(1)
							.text()));
					vo.setAnswerSecondContent(textEles.get(2).toString());
					vo.setAnswerSecondVote(Integer.valueOf(voteEles.get(2)
							.text()));
				} else if (textEles.size() == 2) {
					vo.setQuestionContent(textEles.get(0).toString());
					vo.setQuetionVote(Integer.valueOf(voteEles.get(0).text()));
					vo.setAnswerFirstContent(textEles.get(1).toString());
					vo.setAnswerFirstVote(Integer.valueOf(voteEles.get(1)
							.text()));
					vo.setAnswerSecondContent("");
					vo.setAnswerSecondVote(0);
				} else if (textEles.size() == 1) {
					vo.setQuestionContent(textEles.get(0).toString());
					vo.setQuetionVote(Integer.valueOf(voteEles.get(0).text()));
					vo.setAnswerFirstContent("");
					vo.setAnswerFirstVote(0);
					vo.setAnswerSecondContent("");
					vo.setAnswerSecondVote(0);
				} else {
					vo.setQuestionContent("");
					vo.setQuetionVote(0);
					vo.setAnswerFirstContent("");
					vo.setAnswerFirstVote(0);
					vo.setAnswerSecondContent("");
					vo.setAnswerSecondVote(0);
				}
				// tag
				Elements tagEles = doc.getElementsByClass("post-taglist");
				String tag = tagEles.size() == 0 ? "" : tagEles.get(0).text();
				vo.setTag(tag);

				// time
				Elements timeEles = doc.select("p.label-key");
				String time = timeEles.size() <= 1 ? "" : timeEles.get(1)
						.attributes().get("title");
				vo.setQuestionTime(time);

				stackOverFlowDao.save(vo);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("内容转换出错了：" + e);
			}
		}
	}
}

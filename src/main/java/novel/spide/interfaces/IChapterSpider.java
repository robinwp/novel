package novel.spide.interfaces;

import java.io.IOException;
import java.util.List;

import novel.spide.pojo.Chapter;

public interface IChapterSpider {
	/**
	 * 给我们一个完整的url，我们给你返回章节列表
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	public List<Chapter> getChapter(String url);
}

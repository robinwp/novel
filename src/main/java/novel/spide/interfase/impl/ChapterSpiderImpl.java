package novel.spide.interfase.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import novel.spide.enums.NovelSiteEnum;
import novel.spide.interfaces.IChapterSpider;
import novel.spide.pojo.Chapter;
import novel.spide.util.NovelSpiderUtil;

public class ChapterSpiderImpl extends ChapterSpiderCrawlImpl implements IChapterSpider {

	/**
	 * 给我一个url，返回章节列表
	 */
	public List<Chapter> getChapter(String url) {
		String result;
		try {
			result = crawl(url);
			Document document = Jsoup.parse(result);
			document.setBaseUri(url);
			Elements elements = document
					.select(NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("chapter-list-selector"));
			List<Chapter> chapters = new ArrayList<>();
			for (Element e : elements) {
				Chapter chapter = new Chapter();
				chapter.setTitle(e.text());
				chapter.setUrl(e.absUrl("href"));
				chapters.add(chapter);
			}
			return chapters;
		} catch (IOException e) {
			System.err.println("尝试获取小说列表失败！");
			throw new RuntimeException(e);
		}
	}

}

package novel.spide.interfase.impl;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import novel.spide.enums.NovelSiteEnum;
import novel.spide.interfaces.IChapterDetailSpider;
import novel.spide.pojo.ChapterDetail;
import novel.spide.util.NovelSpiderUtil;

public class ChapterDetailSpiderImpl extends ChapterSpiderCrawlImpl implements IChapterDetailSpider {

	/**
	 * 给我一个url,返回章节内容
	 */
	public ChapterDetail getChapterDetail(String url) {
		try {
			String result = crawl(url).replace("&nbsp;", " ").replace("<br />", "${line}").replace("<br/>", "${line}");
			Document document = Jsoup.parse(result);
			document.setBaseUri(url);
			Map<String, String> contexts = NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url));
			String[] titleSelector = getSeletor(contexts.get("chapter-detail-title-selector"));
			ChapterDetail chapterDetail = new ChapterDetail();
			chapterDetail.setTitle(document.select(titleSelector[0]).get(Integer.parseInt(titleSelector[1])).text());
			String contentSelector = contexts.get("chapter-detail-content-selector");
			chapterDetail.setContent(document.select(contentSelector).first().text().replace("${line}", "\n"));
			String[] prevSelector = getSeletor(contexts.get("chapter-detail-prev-selector"));
			chapterDetail
					.setPrve(document.select(prevSelector[0]).get(Integer.parseInt(prevSelector[1])).absUrl("href"));
			String[] nextSelector = getSeletor(contexts.get("chapter-detail-next-selector"));
			chapterDetail
					.setNext(document.select(nextSelector[0]).get(Integer.parseInt(nextSelector[1])).absUrl("href"));
			return chapterDetail;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private String[] getSeletor(String selector) {
		int a = selector.indexOf(",");
		if (a != -1) {
			return new String[] { selector.substring(0, a), selector.substring(a + 1) };
		} else {
			return new String[] { selector, "0" };
		}
	}
}

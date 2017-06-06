package novel.spide.interfase.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import novel.spide.enums.NovelSiteEnum;
import novel.spide.interfaces.INovelSpider;
import novel.spide.pojo.Novel;
import novel.spide.util.NovelSpiderUtil;

public abstract class NovelSpiderImpl extends ChapterSpiderCrawlImpl implements INovelSpider {
	protected Element nextPageElement;
	protected String nextPage;

	protected Elements getsTr(String url) {
		try {
			String result = crawl(url);
			String novelSelector = NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("novel-selector");
			if (novelSelector == null)
				throw new RuntimeException("url=" + url + "目前不支持抓取该网站小说列表！");
			Document document = Jsoup.parse(result);
			document.setBaseUri(url);
			String nextPageSelector = NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url))
					.get("novel-next-select");
			if (nextPageSelector != null) {
				Elements nextPageElements = document.select(nextPageSelector);
				nextPageElement = nextPageElements == null ? null : nextPageElements.first();
				nextPage = nextPageElement == null ? "" : nextPageElement.absUrl("href");
			}
			return document.select(novelSelector);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasNext() {
		return !nextPage.isEmpty();
	}

	@Override
	public String next() {
		return nextPage;
	}

	@Override
	public Iterator<List<Novel>> iterator(String firstPath) {
		nextPage = firstPath;
		return new NovleIterator();

	}

	/**
	 * 一个迭代器，专门用户对小说书籍列表抓取
	 * 
	 * @author 墨迹
	 *
	 */
	private class NovleIterator implements Iterator<List<Novel>> {
		@Override
		public boolean hasNext() {
			return NovelSpiderImpl.this.hasNext();
		}

		@Override
		public List<Novel> next() {
			return getNovel(nextPage);
		}

	}

}

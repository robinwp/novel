package novel.spide.interfase.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import novel.spide.enums.NovelSiteEnum;
import novel.spide.pojo.Novel;
import novel.spide.util.LogManager;
import novel.spide.util.NovelSpiderUtil;

public class DdxsNovelSpider extends NovelSpiderImpl {

	public List<Novel> getNovel(String url) {
		for (int j = 0; j < NovelSpiderImpl.MAX_TRY_TIMES; j++) {
			try {
				List<Novel> novels = new ArrayList<>();
				Elements trs = getsTr(url);
				for (int i = 1, size = trs.size(); i < size; i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					Novel novel = new Novel();
					novel.setName(tds.first().getElementsByTag("a").get(1).text());
					novel.setUrl(tds.first().getElementsByTag("a").get(1).absUrl("href"));
					novel.setAuthor(tds.get(2).text());
					novel.setLastUpdateTime(NovelSpiderUtil.getNovelDate(tds.get(4).text(), "yy-MM-dd"));
					novel.setStatus(NovelSpiderUtil.getNovelStatus(tds.get(5).text()));
					novel.setLastUpdateChapter(tds.get(1).getElementsByTag("a").first().text());
					novel.setLastUpdateChapterUrl(tds.get(1).getElementsByTag("a").first().absUrl("href"));
					novel.setNovelFormId(NovelSiteEnum.getEnumByUrl(url).getId());
					novel.setAddTime(new Date());
					novels.add(novel);
				}
				return novels;
			} catch (RuntimeException e) {
				System.err.println("尝试第" + (j + 1) + "次抓取" + url + "小说列表失败！");
				LogManager.warnToFile(null, "尝试第" + (j + 1) + "次抓取" + url + "小说列表失败！");
			}
		}
		return null;
	}

}

package novel.spide.interfase.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.select.Elements;

import novel.spide.enums.NovelSiteEnum;
import novel.spide.pojo.Novel;
import novel.spide.util.LogManager;
import novel.spide.util.NovelSpiderUtil;

public class KszNovelSpider extends NovelSpiderImpl {

	public List<Novel> getNovel(String url) {
		for (int j = 0; j < NovelSpiderImpl.MAX_TRY_TIMES; j++) {
			try {
				List<Novel> novels = new ArrayList<>();
				Elements trs = getsTr(url);
				for (int i = 1, size = trs.size()-1; i < size; i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					Novel novel = new Novel();
					novel.setType(tds.first().getElementsByTag("span").first().text());
					novel.setName(tds.get(1).getElementsByTag("span").first().text());
					novel.setUrl(tds.get(1).child(0).absUrl("href"));
					novel.setAuthor(tds.get(3).getElementsByTag("span").first().text());
					novel.setLastUpdateTime(
							NovelSpiderUtil.getNovelDate(tds.get(4).getElementsByTag("span").first().text(), "MM-dd"));
					novel.setStatus(NovelSpiderUtil.getNovelStatus(tds.get(5).getElementsByTag("span").first().text()));
					novel.setLastUpdateChapter(tds.get(2).getElementsByTag("span").first().text());
					novel.setLastUpdateChapterUrl(tds.get(2).child(0).absUrl("href"));
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

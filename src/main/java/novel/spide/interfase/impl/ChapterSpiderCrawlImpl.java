package novel.spide.interfase.impl;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import novel.spide.enums.NovelSiteEnum;
import novel.spide.interfaces.IChapterSpiderCrawl;
import novel.spide.util.NovelSpiderUtil;
import novel.spide.util.NovelSpiderHttpGetUtil;

public class ChapterSpiderCrawlImpl implements IChapterSpiderCrawl {

	/**
	 * 返回网页内容
	 */
	@Override
	public String crawl(String url) throws IOException {
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse httpResponse = httpClient.execute(new NovelSpiderHttpGetUtil(url));) {
			return EntityUtils.toString(httpResponse.getEntity(),
					NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("charset"));
		} catch (IOException e) {
			throw new RuntimeException("IP被封了");
		}
	}

}

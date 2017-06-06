package novel.spide.factory;

import novel.spide.enums.NovelSiteEnum;
import novel.spide.interfaces.INovelSpider;
import novel.spide.interfase.impl.DdxsNovelSpider;
import novel.spide.interfase.impl.KszNovelSpider;

public final class NovelSpideFactory {
	private NovelSpideFactory(){};
	
	public static INovelSpider getNovelSpider(String url){
		NovelSiteEnum novelSiteEnum=NovelSiteEnum.getEnumByUrl(url);
		switch(novelSiteEnum){
			case dingdianxiaoshuo: return new DdxsNovelSpider();
			case kanshuozhong: return new KszNovelSpider();
			default: throw new RuntimeException("url="+url+"暂时不被支持！");
		}
	}
	
}

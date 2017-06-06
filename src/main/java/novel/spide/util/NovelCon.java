package novel.spide.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import novel.spide.pojo.Novel;

/**
 * 作者 墨迹 on 2017/4/25.
 *
 * 获取小说列表
 *
 *
 */
public class NovelCon {

    public static List<Novel> getNovelsByKey(String key, int page) throws IOException, JSONException {
        if (page < 1) {
            page = 1;
        }
        if (key == null) {
            key = "";
        } else {
            key = URLEncoder.encode(key, "utf-8");
        }
        return getNovels("http://118.89.44.108/novel-web/android/selectNovel.html?key=" + key + "&page=" + page);
    }

    private static List<Novel> getNovels(String novelurl) throws IOException, JSONException {
        URL url = new URL(novelurl);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
        urlConnection.setConnectTimeout(5_000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = urlConnection.getInputStream();
        byte[] buff = new byte[1024];
        int len;
        while ((len = in.read(buff, 0, 1024)) != -1) {
            baos.write(buff, 0, len);
        }
        String info = new String(baos.toByteArray(), "UTF-8");
        List<Novel> novels = new ArrayList<>();
        if (info.equals("[]")) {
            return null;
        } else {
            JSONArray jsonArray = JSONArray.fromObject(info);
            for (int i = 0, size = jsonArray.size(); i < size; i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                Novel novel = new Novel();
                novel.setAuthor(json.optString("author"));
                novel.setStatus(Integer.valueOf(json.optString("status")));
                novel.setUrl(json.getString("url"));
                novel.setName(json.getString("name"));
                novels.add(novel);
            }
            return novels;
        }
    }
}

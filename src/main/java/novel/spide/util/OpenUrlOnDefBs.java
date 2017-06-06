package novel.spide.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

public class OpenUrlOnDefBs {

	public static void opUrl(String url) {
		url = (url == null || url.equals("")) ? "http://118.89.44.108" : url;
		if (!Desktop.isDesktopSupported()) {
			// 测试当前平台是否支持此类
			JOptionPane.showMessageDialog(null, "浏览器设置不支持，请手动输入链接：\n http://118.89.44.108");
			return;
		}
		// 用来打开系统默认浏览器浏览指定的URL
		Desktop desktop = Desktop.getDesktop();
		URI uri;
		try {
			uri = new URI(url);
			desktop.browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

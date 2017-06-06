package novel.spide.view;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import novel.spide.configuration.Configuration;
import novel.spide.interfaces.NovelDownload;
import novel.spide.interfase.impl.NovelDownloadImpl;
import novel.spide.pojo.Novel;
import novel.spide.util.LogManager;
import novel.spide.util.NovelCon;
import novel.spide.util.OpenUrlOnDefBs;

public class MainView {

	protected Shell shell;
	private Text text_name;
	private List<Novel> novels = new ArrayList<>();
	private org.eclipse.swt.widgets.List list_novels;
	private MessageBox mes;
	private Button btn_next;
	private Button btn_prev;
	private int page = 1;
	private Button btn_down;
	private FileDialog fileSave;
	private Label lab_status;
	private Display display;
	private boolean downing = false;
	private Button btn_select;
	private Link link;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainView window = new MainView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN);
		shell.setImage(SWTResourceManager.getImage("img/book.ico"));
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				if (downing) {
					e.doit = false;
					mes = new MessageBox(shell, SWT.ICON_WARNING);
					mes.setText("提示");
					mes.setMessage("还有下载任务在进行");
					mes.open();
				}
			}
		});
		shell.setSize(450, 288);
		shell.setText("墨迹小说下载器");
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;
		if (shellH > screenH)
			shellH = screenH;
		if (shellW > screenW)
			shellW = screenW;
		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2) - 50);
		shell.setLayout(null);

		fileSave = new FileDialog(shell, SWT.SAVE);
		fileSave.setText("保存");
		fileSave.setFilterNames(new String[] { "*.txt" });
		fileSave.setFilterExtensions(new String[] { "*.txt" });
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lblNewLabel.setBounds(10, 10, 192, 27);
		lblNewLabel.setText("请输入要下载的小说名称：");
		text_name = new Text(shell, SWT.BORDER);
		text_name.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					selectClick();
				}
			}
		});
		text_name.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		text_name.setBounds(208, 10, 149, 27);

		btn_select = new Button(shell, SWT.NONE);
		btn_select.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseEnter(MouseEvent e) {
				btn_select.setCursor(new Cursor(null, SWT.CURSOR_HAND));
			}
		});
		btn_select.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					selectClick();
				}
			}
		});
		btn_select.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		btn_select.setBounds(363, 10, 71, 27);
		btn_select.setText("搜索");
		list_novels = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list_novels.setBounds(10, 43, 424, 158);
		btn_next = new Button(shell, SWT.NONE);
		btn_next.setEnabled(false);
		btn_next.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					try {
						page += 1;
						novels = NovelCon.getNovelsByKey(text_name.getText(), page);
						if (novels == null) {
							mes = new MessageBox(shell, SWT.ICON_WARNING);
							mes.setText("提示");
							mes.setMessage("已经是最后一页了");
							mes.open();
							return;
						}
						list_novels.removeAll();
						for (int i = 0, size = novels.size(); i < size; i++) {
							list_novels.add(novels.get(i).toString());
						}
					} catch (Exception e1) {
						mes = new MessageBox(shell, SWT.ICON_ERROR);
						mes.setText("错误");
						mes.setMessage("网络连接失败！请检查网络连接，或稍后再试！");
						mes.open();
					}
				}
			}
		});
		btn_next.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseEnter(MouseEvent e) {
				btn_next.setCursor(new Cursor(null, SWT.CURSOR_HAND));
			}
		});
		btn_next.setText("下一页");
		btn_next.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		btn_next.setBounds(363, 207, 71, 27);

		btn_prev = new Button(shell, SWT.NONE);
		btn_prev.setEnabled(false);
		btn_prev.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					try {
						if (page == 1) {
							mes = new MessageBox(shell, SWT.ICON_WARNING);
							mes.setText("提示");
							mes.setMessage("已经是第一页了");
							mes.open();
							return;
						}
						page -= 1;
						novels = NovelCon.getNovelsByKey(text_name.getText(), page);
						list_novels.removeAll();
						for (int i = 0, size = novels.size(); i < size; i++) {
							list_novels.add(novels.get(i).toString());
						}
					} catch (Exception e1) {
						mes = new MessageBox(shell, SWT.ICON_ERROR);
						mes.setText("错误");
						mes.setMessage("网络连接失败！请检查网络连接，或稍后再试！");
						mes.open();
					}
				}
			}
		});
		btn_prev.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseEnter(MouseEvent e) {
				btn_prev.setCursor(new Cursor(null, SWT.CURSOR_HAND));
			}
		});
		btn_prev.setText("上一页");
		btn_prev.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		btn_prev.setBounds(286, 207, 71, 27);

		btn_down = new Button(shell, SWT.NONE);
		btn_down.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					int index = list_novels.getSelectionIndex();
					if (index == -1) {
						mes = new MessageBox(shell, SWT.ICON_WARNING);
						mes.setText("提示");
						mes.setMessage("请选择要下载的小说！");
						mes.open();
						return;
					}
					downNovel(novels.get(index).getUrl());
				}
			}
		});
		btn_down.setText("下载");
		btn_down.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		btn_down.setEnabled(false);
		btn_down.setBounds(10, 207, 71, 27);

		lab_status = new Label(shell, SWT.NONE);
		lab_status.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		lab_status.setBounds(94, 213, 108, 17);
		
		link = new Link(shell, SWT.NONE);
		link.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if(e.button == 1){
					OpenUrlOnDefBs.opUrl("http://118.89.44.108/novel-web");
				}
				
			}
		});
		link.setBounds(381, 240, 53, 17);
		link.setText("<a>by 墨迹</a>");

	}

	private void selectClick() {
		try {
			page = 1;
			novels = NovelCon.getNovelsByKey(text_name.getText(), page);
			if (novels != null) {
				list_novels.removeAll();
				for (int i = 0, size = novels.size(); i < size; i++) {
					list_novels.add(novels.get(i).toString());
				}
				btn_down.setEnabled(true);
				btn_next.setEnabled(true);
				btn_prev.setEnabled(true);
			} else {
				mes = new MessageBox(shell, SWT.ICON_WARNING);
				mes.setText("提示");
				mes.setMessage("没有搜索到小说，请检查小说名称");
				mes.open();
				btn_down.setEnabled(false);
				btn_next.setEnabled(false);
				btn_prev.setEnabled(false);
			}
		} catch (Exception e1) {
			mes = new MessageBox(shell, SWT.ICON_ERROR);
			mes.setText("错误");
			mes.setMessage("网络连接失败！请检查网络连接，或稍后再试！");
			mes.open();
		}
	}

	public void updateInfo(String info) {
		display.asyncExec(new Runnable() {
			public void run() {
				list_novels.add(info);
				list_novels.setTopIndex(list_novels.getItemCount());
			}
		});
	}

	private void downNovel(String url) {
		String path = fileSave.open();
		if (path != null) {
			downing = true;
			NovelDownload novelDownload = new NovelDownloadImpl(this);
			display.syncExec(new Runnable() {
				public void run() {
					btn_select.setEnabled(false);
					btn_down.setEnabled(false);
					btn_next.setEnabled(false);
					btn_prev.setEnabled(false);
					lab_status.setText("下载中···");
					list_novels.removeAll();
				}
			});
			new Thread(new Runnable() {
				public void run() {
					novelDownload.download(url, new Configuration(path));
					downing = false;
					display.syncExec(new Runnable() {
						public void run() {
							lab_status.setText("下载完成");
							btn_select.setEnabled(true);
							if (LogManager.logwarn != null) {
								for (int i = 0, size = LogManager.logwarn.size(); i < size; i++) {
									list_novels.add(LogManager.logwarn.get(i));
									list_novels.setTopIndex(list_novels.getItemCount());
								}
							}
							new Thread(new Runnable() {
								public void run() {
									try {
										Thread.sleep(3_000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									display.syncExec(new Runnable() {
										public void run() {
											lab_status.setText("");
										}
									});
								}
							}).start();
						}
					});
				}
			}).start();

		}
	}
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.io.*;
import java.util.ArrayList;

//GUI開発部品のベースとなる部分。　イベントもここで扱う

public class Base extends JFrame implements ChangeListener{
	static final int EDITOR_WIDTH = 640;
	static final int EDITOR_HEIGHT = 480;
	static final int HOLDER_NUMBER = 6;

	static GridBagConstraints grid = new GridBagConstraints();
	
	private JViewport view;		//
	private JScrollPane sc;		//マップエディタGUI
	private JPanel menuPanel;	//メニューパネルGUI
	private JPanel headerPanel;
	private JPanel holderPanel;
	private JPanel pn;
	private JPanel footerPanel;

	private ButtonPanel bp;		//ボタン用のパネル ////////////////
	private Tipselector tp;		//チップ選択
	private Editor ed;			//マップエディタ
	private Tipholder th[];
	private Tipselector ts;
	private Mapprop prop;
	private Newmap nm;
	private ArrayList<Layerselector> ls;
	private Footer ft;
	private int editLayer;

	private int preX;
	private int preY;

	public Base(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Mapmaker");
		setLayout(new BorderLayout());

		headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(2,1));
		add(headerPanel, BorderLayout.NORTH);
		
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(1,2));
		headerPanel.add(menuPanel);

		prop = new Mapprop();
		ls = new ArrayList<Layerselector>();

		ButtonInit();
		TipHolderInit();
		LayerListInit();
		MapEditorInit(null);
		FooterInit();

		pack();
		setResizable(false);
		setVisible(true);
	}

	private void ButtonInit(){//ボタンパネルを作成
		bp = new ButtonPanel(this);
		menuPanel.add(bp);
	}

	private void TipHolderInit(){//チップホルダーを作成
		th = new Tipholder[HOLDER_NUMBER];
		holderPanel = new JPanel();
		
		holderPanel.setLayout(new FlowLayout());
		
		for(int i = 0 ; i < HOLDER_NUMBER - 1 ; i++){
			th[i] = new SelctedTipholder(this);
			holderPanel.add(th[i]);
		}

		th[HOLDER_NUMBER - 1] = new NavTipholder(this);
		holderPanel.add(th[HOLDER_NUMBER - 1]);

		menuPanel.add(holderPanel);
	}

	private void LayerListInit(){//レイヤーリストを作成
		pn = new JPanel();
		pn.setLayout(new FlowLayout(FlowLayout.LEFT));
		editLayer = -1;

		for(int i = 0 ; i < prop.getLayerSum() ; i++){
			Layerselector temp = new Layerselector(this);
			ls.add(temp);
			pn.add(temp);
		}

		headerPanel.add(pn);
	}

	private void MapEditorInit(String file){//マップエディタを作成
		sc = new JScrollPane();
		sc.setPreferredSize(new Dimension(EDITOR_WIDTH, EDITOR_HEIGHT));

		JScrollBar barX = sc.getHorizontalScrollBar();
		JScrollBar barY = sc.getVerticalScrollBar();

		barX.setMaximum(prop.getMapSizeX() * prop.getTipSizeX() - EDITOR_WIDTH);
		barY.setMaximum(prop.getMapSizeY() * prop.getTipSizeY() - EDITOR_HEIGHT);

		if(file == null){
			ed = new Editor(this);
		}else{
			ed = new Editor(this, file);
		}
		sc.getViewport().add(ed);//あとからパネルなどを追加するにはこうする
		sc.getViewport().addChangeListener(this);

		this.add(sc, BorderLayout.CENTER);
	}

	private void FooterInit(){
		footerPanel = new JPanel();
		footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		ft = new Footer(this);
		footerPanel.add(ft);

		this.add(footerPanel, BorderLayout.SOUTH);
	}
	/////////////////////////////////////////////////

	public void TipselectorInit(){//チップ選択
		ts = new Tipselector(this);
	}

	public void newMapDialog(){
		nm = new Newmap(this);
	}

	public void setMapEditor(Mapprop mp, String file){
		prop = mp;

		for(int i = 0 ; i < HOLDER_NUMBER ; i++){
			holderPanel.remove(th[i]);
		}
		menuPanel.remove(holderPanel);

		for(int i = 0 ; i < ls.size() ; i++){
			pn.remove(ls.get(i));
		}
		ls.clear();
		headerPanel.remove(pn);
		remove(sc);

		TipHolderInit();
		LayerListInit();
		MapEditorInit(file);
		ft.renewLabel(mp);

		validate();/////////////ここ重要!!!!!!!!!!!!!!!!
	}

	public Mapprop getMapProp(){
		return prop;
	}

	public void setTipNum(int i){
		ed.setSelectedTip(i);
		Tipholder.setTip(i);
	}
	public void setTipNumByHolder(int i){
		ed.setSelectedTip(i);
	}

	public void moveScroll(int x, int y){
		JScrollBar barX = sc.getHorizontalScrollBar();
		JScrollBar barY = sc.getVerticalScrollBar();

		barX.setValue(barX.getValue() - x);
		barY.setValue(barY.getValue() - y);
	}

	public ArrayList<Layerselector> getLayerList(){
		return ls;
	}

	public void setVisibleLayer(){
		sc.repaint();
	}

	public void setSelectedLayer(Layerselector layer){
		int i;

		if(layer == null){
			editLayer = -1;
		}else{
			for(i = 0 ; i < ls.size() ; i++){
				if(ls.get(i) == layer) break;
			}

			if(i >= ls.size()) editLayer = -1;
			editLayer = i;
		}
		ft.setLayerLabel(editLayer, prop.getLayerSum());
		sc.repaint();
	}

	public int getSelectedLayer(){
		return editLayer;
	}

	public Footer getFooter(){
		return ft;
	}

	public void save(File file){
		if(file == null) return;

		/*
		 * フォルダが選択される。
		 * それに、各レイヤーの配列をファイルとして保存(別々に)。
		 * また、ステージの属性情報ファイルも保存。
		 * 終了。
		 *
		 * 読み込みは逆の操作で。
		 */

		String mapDirPath = file.getAbsolutePath() + "/map_" +  Integer.toString(prop.getID());
		File mapDir = new File(mapDirPath);
		mapDir.mkdir();//マップフォルダ作成

		ed.save(mapDirPath);
		prop.save(mapDirPath);
	}

	public void load(File file){
		if(file == null) return;

		//マップがあるフォルダが選択される。

		Mapprop temp = new Mapprop(file);

		if(temp.isError() == false){
			prop = temp;
			setMapEditor(prop, file.getAbsolutePath());
		}
	}

	/*-------------イベント関連 SATRT-------------------*/

	public void stateChanged(ChangeEvent e){
		
		//スクロール関連
		if(e.getSource() == sc.getViewport()){
			view = sc.getViewport();
			Point pt = view.getViewPosition();
			ed.setScroll(pt);
			sc.repaint();
		}
	}
	/*-------------イベント関連 END-------------------*/
}

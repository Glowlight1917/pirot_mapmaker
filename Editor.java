import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

import java.io.*;
import java.util.ArrayList;

class EditorCalc{//マップエディタ
	private ArrayList<Map> mapList;

	private int scrollX;
	private int scrollY;

	private int selectedMapX;
	private int selectedMapY;

	private int mapSizeX;
	private int mapSizeY;
	private int tipSizeX;
	private int tipSizeY;
	private int layerSum;

	public EditorCalc(Base bs){
		init(bs);
		
		mapList = new ArrayList<Map>();
		for(int i = 0 ; i < layerSum ; i++){
			mapList.add(new Map(mapSizeX, mapSizeY));
		}
	}

	public EditorCalc(Base bs, String file){
		init(bs);

		mapList = new ArrayList<Map>();
		for(int i = 0 ; i < layerSum ; i++){
			mapList.add(new Map(file + "/" + Integer.toString(i) + ".map"));
		}
	}

	private void init(Base bs){
		Mapprop mp = bs.getMapProp();
		mapSizeX = mp.getMapSizeX();
		mapSizeY = mp.getMapSizeY();
		tipSizeX = mp.getTipSizeX();
		tipSizeY = mp.getTipSizeY();
		layerSum = mp.getLayerSum();
	}

	public void setScroll(int x, int y){//スクロール量を設定
		scrollX = x;
		scrollY = y;
	}

	public int getScrollX(){
		return scrollX;
	}
	
	public int getScrollY(){
		return scrollY;
	}

	public int getTipNumber(int x, int y, int layer){//指定した座標にあるチップの番号を取得
		Map map = mapList.get(layer);
		return  map.get(x, y);
	}

	public int getMapPosX(int x){
		return x / tipSizeX;
	}

	public int getMapPosY(int y){
		return y / tipSizeY;
	}

	public void save(String file){
		String fileName;

		for(int i = 0 ; i < mapList.size() ; i++){
			fileName = file + "/" + Integer.toString(i) + ".map";
			mapList.get(i).save(fileName);
		}
	}

	public void setTip(int x, int y, int tip, int layer){//指定した座標にチップを設置
		if(0 <= x  && x < tipSizeX * mapSizeX && 0 <= y && y < tipSizeY * mapSizeY){
				selectedMapX = x / tipSizeX;
				selectedMapY = y / tipSizeY;

				mapList.get(layer).set(selectedMapX, selectedMapY, tip);
		}
	}
}

class Editor extends JPanel implements ActionListener{
	private EditorCalc calc;
	private int selectedTip;
	private static Base bs;

	public Editor(Base bs) {
		this.bs = bs;
		calc = new EditorCalc(bs);

		init();
	}

	public Editor(Base bs, String file){
		this.bs = bs;
		calc = new EditorCalc(bs, file);
		
		init();
	}

	private void init(){
		Mapprop mp = bs.getMapProp();
		
		int width = mp.getMapSizeX() * mp.getTipSizeX();
		int height = mp.getMapSizeY() * mp.getTipSizeY();

		setFocusable(true);
		setDoubleBuffered(true);
		setPreferredSize(new Dimension(width, height));
		setBackground(new Color(0,255,0,0));
		
		addMouseMotionListener(new MouseAdp());
		addMouseListener(new MouseAdp());
	}

	public void paint(Graphics g) {//描画
		super.paint(g);

		Graphics2D g2d = (Graphics2D)g;
		Mapprop mp = bs.getMapProp();
		int mapSizeX = mp.getMapSizeX();
		int mapSizeY = mp.getMapSizeY();
		int tipSizeX = mp.getTipSizeX();
		int tipSizeY = mp.getTipSizeY();

		g2d.setBackground(Color.white);
		g2d.clearRect(0, 0, getWidth(), getHeight());

		int scX = calc.getScrollX();
		int scY = calc.getScrollY();
		int initX = scX / tipSizeX;
		int initY = scY / tipSizeY;
		int endX = (scX + 640) / tipSizeX;
		int endY = (scY + 480) / tipSizeY;
		int divTipLeng = mp.DivTipLength();

		g2d.setColor(Color.lightGray);

		ArrayList<Layerselector> ls = bs.getLayerList();

		for(int x = initX ; x <= endX ; x++){
			g2d.drawLine(x * tipSizeX, initY * tipSizeY, x * tipSizeX, endY * tipSizeY);
		}
		for(int y = initY ; y <= endY ; y++){
			g2d.drawLine(initX * tipSizeX, y * tipSizeY, endX * tipSizeX, y * tipSizeY);
		}

		for(int i = 0 ; i < ls.size() ; i++){
			if(ls.get(i).isLayerVisible() == false) continue;
			
			for(int y = initY ; y <= endY ; y++){
				for(int x = initX ; x <= endX ; x++){
					
					int tip = calc.getTipNumber(x, y, i);
					if(tip > 0 && tip < divTipLeng){
						g2d.drawImage(mp.getDivTip(tip),x * tipSizeX, y * tipSizeY, this);
					}
				}
			}
		}
			
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void actionPerformed(ActionEvent e) {//毎回呼ばれる場所　処理部分
		repaint();
	}

    public void setScroll(Point pt){
		calc.setScroll(pt.x, pt.y);
	}

	public void setSelectedTip(int i){
		selectedTip = i;
	}

	public void save(String file){
		calc.save(file);
	}

	class MouseAdp extends MouseAdapter{
		int movX;
		int movY;

		public void mousePressed(MouseEvent e){//クリックしたマップの座標を取得
			draw(e);
		}

		public void mouseDragged(MouseEvent e){
			draw(e);
		}

		public void mouseMoved(MouseEvent e){
			bs.getFooter().setPosLabel(calc.getMapPosX(e.getX()), calc.getMapPosY(e.getY()));
		}

		public void draw(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			int layer = bs.getSelectedLayer();

			if(layer != -1){
			
				if(SwingUtilities.isLeftMouseButton(e) == true){
					calc.setTip(x, y, selectedTip, layer);
				}

				if(SwingUtilities.isRightMouseButton(e) == true){
					calc.setTip(x, y, 0, layer);
				}

				repaint();
			}
		}
	}
}

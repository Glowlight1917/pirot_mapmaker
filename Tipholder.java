import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.*;
import java.util.ArrayList;

class TipholderCalc{
	
}

class Tipholder extends JPanel{
	static final int BORDER= 2;
	protected int tipSizeX;
	protected int tipSizeY;
	protected int selectedTip;
	protected static Tipholder nav;
	protected static Tipholder clicked;
	protected static Base bs;

	protected static LineBorder gray = new LineBorder(Color.gray, BORDER);
	protected static LineBorder lightGray = new LineBorder(Color.lightGray, BORDER);
	
	public Tipholder(Base bs){
		this.bs = bs;
		Mapprop mp = bs.getMapProp();
		tipSizeX = mp.getTipSizeX();
		tipSizeY = mp.getTipSizeY();
		selectedTip = 0;

		this.setPreferredSize(new Dimension(tipSizeX + 2 * BORDER, tipSizeY + 2 * BORDER));
	}

	static public void setTip(int i){
		if(clicked != null){
			clicked.selectedTip = i;
			clicked.setBorder(lightGray);
			clicked.repaint();
		}
		nav.selectedTip = i;
		nav.repaint();
		clicked = null;
	}

	@Override public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		Mapprop mp = bs.getMapProp();

		g2d.drawImage(mp.getDivTip(selectedTip), BORDER, BORDER, this);
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
}

class NavTipholder extends Tipholder{//現在選択されているチップを表示
	public NavTipholder(Base bs){
		super(bs);

		nav = this;
		setBorder(new LineBorder(Color.pink, BORDER));
	}
}

class SelctedTipholder extends Tipholder{//保持されているチップを表示
	
	public SelctedTipholder(Base bs){
		super(bs);

		addMouseListener(new MouseAdp());
		setBorder(lightGray);
	}

	private void setClickedHolder(){
		if(clicked != null){
			clicked.setBorder(lightGray);
		} 
		clicked = this;
		setBorder(gray);
	}

	private void setCurrentTip(){
		nav.selectedTip = this.selectedTip;
		bs.setTipNumByHolder(selectedTip);
		nav.repaint();
	}

	class MouseAdp extends MouseAdapter{
		public void mousePressed(MouseEvent e){
			setClickedHolder();
			setCurrentTip();
		}
	}
}

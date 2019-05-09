import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.ArrayList;

class TipselectorCalc{
	public int getTipNum(int x, int y, Base bs){
		Mapprop mp = bs.getMapProp();

		int tipNumX = mp.getTipNumX();
		int tipNumY = mp.getTipNumY();
		int tipSizeX = mp.getTipSizeX();
		int tipSizeY = mp.getTipSizeY();
		
		if(x < tipSizeX * tipNumX && x >= 0 && y < tipSizeY * tipNumY && y >= 0){
			return (x / tipSizeX) + (y / tipSizeY) * tipNumX;	
		}

		return 0;
	}
}

class TipselectorPane extends JPanel implements ActionListener{
	private TipselectorCalc calc;
	private static Base bs;

	public TipselectorPane(Base bs){
		this.bs = bs;
		calc = new TipselectorCalc();
		
		addMouseMotionListener(new MouseAdp());
		addMouseListener(new MouseAdp());


		Mapprop mp = bs.getMapProp();
		int width = mp.getTipSizeX() * mp.getTipNumX();
		int height = mp.getTipSizeY() * mp.getTipNumY();
		
		setPreferredSize(new Dimension(width, height));
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	@Override public void paint(Graphics g) {//マップの描画
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;

		g2d.drawImage(bs.getMapProp().getTipImg(), 0, 0, this);
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	
	class MouseAdp extends MouseAdapter{
		public void mouseDragged(MouseEvent e){
			getTipNum(e);
		}

		public void mousePressed(MouseEvent e){
			getTipNum(e);
		}

		public void getTipNum(MouseEvent e){			
			bs.setTipNum(calc.getTipNum(e.getX(), e.getY(), bs));
		}
	}
}

class Tipselector extends JFrame{
	static final int SELECTOR_WIDTH = 300;
	static final int SELECTOR_HEIGHT = 300;
	private static Base bs = null;
	private TipselectorPane tp;
	private static boolean opened = false;

	public Tipselector(Base bs){
		if(this.bs == null ) this.bs = bs;

		opened = true;
		JScrollPane tipsc = new JScrollPane();
		tipsc.setPreferredSize(new Dimension(SELECTOR_WIDTH, SELECTOR_HEIGHT));
		tipsc.getViewport().add(new TipselectorPane(bs));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("TipSelector");
		add(tipsc);
		pack();
		addWindowListener(new WindowLis());

		setVisible(true);
	}

	public static boolean isOpened(){
		return opened;
	}

	class WindowLis extends WindowAdapter{
		public void windowClosed(WindowEvent e){
			opened = false;
		}
	}
}

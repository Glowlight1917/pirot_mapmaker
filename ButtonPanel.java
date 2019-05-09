import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;

import java.io.*;
import java.util.ArrayList;

public class ButtonPanel extends JPanel implements ActionListener{//ボタンの処理
	private static ImageIcon loadPic = new ImageIcon("./pic/load.png");
	private static ImageIcon savePic = new ImageIcon("./pic/save.png");
	private static ImageIcon newPic = new ImageIcon("./pic/new.png");
	private static ImageIcon tipPic = new ImageIcon("./pic/tip.png");

	private static Insets ins = new Insets(0, 0, 0, 0);

	private JButton save;
	private JButton load;
	private JButton tip;
	private JButton newstage;
	private static Base bs;

	private JFrame tipselector;
	
	public ButtonPanel(Base bs){//ボタン作成
		this.bs = bs;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		save = new JButton(savePic);
		save.setMargin(ins);
		save.setToolTipText("save");
		save.addActionListener(this);
		
		load = new JButton(loadPic);
		load.setMargin(ins);
		load.setToolTipText("load");
		load.addActionListener(this);
		
		tip = new JButton(tipPic);
		tip.setMargin(ins);
		tip.setToolTipText("maptip");
		tip.addActionListener(this);

		newstage = new JButton(newPic);
		newstage.setMargin(ins);
		newstage.setToolTipText("new stage");
		newstage.addActionListener(this);
		
		this.add(save);
		this.add(load);
		this.add(tip);
		this.add(newstage);
	}

	public void actionPerformed(ActionEvent e) {//毎回呼ばれる場所　処理部分
		if(e.getSource() == save){//セーブ
			bs.save(Choose());
		}

		if(e.getSource() == load){//ロード
			bs.load(Choose());
		}
		
		if(e.getSource() == tip){//チップ
			if(Tipselector.isOpened() == false){
				bs.TipselectorInit();
			}
		}

		if(e.getSource() == newstage){//新規作成
			if(Newmap.isOpened() == false){
				bs.newMapDialog();
			}
		}
    }

    private File Choose(){//ディレクトリ読み込みダイアログ
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int selected = chooser.showOpenDialog(this);

		if(selected == chooser.APPROVE_OPTION){
			return chooser.getSelectedFile();
		}

		return null;
	}
}

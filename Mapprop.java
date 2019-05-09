import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

class Mapprop{
	private String name;
	private int tipSizeX;
	private int tipSizeY;
	private int tipNumX;
	private int tipNumY;
	private int mapSizeX;
	private int mapSizeY;
	private int layerSum;
	private int ID;
	private String tipFile;

	private boolean isError;

	private BufferedImage loadImg;
	private BufferedImage vidImg[];

	public Mapprop(){
		name = "test";
		tipSizeX = 32;
		tipSizeY = 32;
		tipNumX  = 10;
		tipNumY  = 10;
		mapSizeX = 60;
		mapSizeY = 60;
		layerSum = 3;
		tipFile = null;
		isError = false;

		loadTipGraph();
	}

	public Mapprop(String file,int x, int y){//新規作成
		isError = false;
		tipSizeX = x;
		tipSizeY = y;
		tipFile = null;
		loadTipGraph();
	}

	public Mapprop(File file){//ロード
		String dataPath = file.getAbsolutePath() + "/data";

		try{
			File fp = new File(dataPath);

			if(fp.exists() == false){
				System.out.println("ファイルが存在しません");
				isError = true;
				return;
			}
			
			FileReader fr = new FileReader(fp);
			BufferedReader br = new BufferedReader(fr);

			String str;
			String data[];
			while((str = br.readLine()) != null){
				data = str.split("=",0);

				switch(data[0]){
					case "ID"		: ID = Integer.parseInt(data[1]); break;
					case "Name"		: name = data[1]; break;
					case "TipSizeX"	: tipSizeX = Integer.parseInt(data[1]); break;
					case "TipSizeY"	: tipSizeY = Integer.parseInt(data[1]); break;
					case "MapSizeX"	: mapSizeX = Integer.parseInt(data[1]); break;
					case "MapSizeY"	: mapSizeY = Integer.parseInt(data[1]); break;
					case "Layer"	: layerSum = Integer.parseInt(data[1]); break;
					case "MapTip"	: tipFile = data[1]; break;
					default:
					{
						System.out.println("Data Error!");
						isError = true;
						return;
					}
				}
			}

			br.close();
			
		}catch(FileNotFoundException e){
			System.out.println(e + "at Glfile ArrayReadInt");
		}catch(IOException e){
			System.out.println(e);
		}

		loadTipGraph();
	}

	private void loadTipGraph(){
		try{
			if(tipFile == null){
				loadImg = ImageIO.read(new File("./pic/map.png"));
				tipFile = "./pic/map.png";
			}else{
				loadImg = ImageIO.read(new File(tipFile));
			}

			tipNumX = loadImg.getWidth() / tipSizeX;
			tipNumY = loadImg.getHeight() / tipSizeY;
			
		}catch(IOException e){
			System.out.println(e);
		}

		//マップチップ画像を分割して読み込む
		//分割した画像を入れるための箱(配列)を作成
		vidImg = new BufferedImage[tipNumX * tipNumY];
		

		for(int i = 0 ; i < tipNumX * tipNumY ; i++){
			vidImg[i] = new BufferedImage(tipSizeX, tipSizeY, BufferedImage.TYPE_4BYTE_ABGR);
		}

		for(int j = 0 ; j < tipNumY ; j++){
			for(int i = 0 ; i < tipNumX ; i++){
				vidImg[j * tipNumX + i] = loadImg.getSubimage(i * tipSizeX, j * tipSizeY, tipSizeX, tipSizeY);
			}
		}
	}

	public BufferedImage getTipImg(){
		return loadImg;
	}

	public BufferedImage getDivTip(int i){
		return vidImg[i];
	}

	public int DivTipLength(){
		return vidImg.length;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setMapSize(int x, int y){
		mapSizeX = x;
		mapSizeY = y;
	}

	public void setLayerSum(int i){
		layerSum = i;
	}

	public void setID(int i){
		ID = i;
	}

	public void save(String file){
		String fileName = file + "/data";
		
		try{
			File fp = new File(fileName);
			
			FileWriter fw = new FileWriter(fp);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("ID=" + ID);
			bw.newLine();
			bw.write("Name=" + name);
			bw.newLine();
			bw.write("TipSizeX=" + Integer.toString(tipSizeX));
			bw.newLine();
			bw.write("TipSizeY=" + Integer.toString(tipSizeY));
			bw.newLine();
			bw.write("MapSizeX=" + Integer.toString(mapSizeX));
			bw.newLine();
			bw.write("MapSizeY=" + Integer.toString(mapSizeY));
			bw.newLine();
			bw.write("Layer=" + Integer.toString(layerSum));
			bw.newLine();
			bw.write("MapTip=" + tipFile);

			bw.close();
		}catch(IOException e){
			System.out.println(e);
		}
		
	}

	public int getMapSizeX(){
		return mapSizeX;
	}

	public int getMapSizeY(){
		return mapSizeY;
	}

	public int getTipSizeX(){
		return tipSizeX;
	}

	public int getTipSizeY(){
		return tipSizeY;
	}

	public int getTipNumX(){
		return tipNumX;
	}

	public int getTipNumY(){
		return tipNumY;
	}

	public int getLayerSum(){
		return layerSum;
	}

	public int getID(){
		return ID;
	}

	public boolean isError(){
		return isError;
	}
}

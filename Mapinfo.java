class Mapinfo{
	private int tipSizeX;
	private int tipSizeY;
	private int mapSizeX;
	private int mapSizeY;

	private int tipNumX;
	private int tipNumY;

	private int layer;
	private int layerSum;
	private int selectedTip;
	
	public Mapinfo(){
		tipSizeX = 32;
		tipSizeY = 32;
		mapSizeX = 200;
		mapSizeY = 60;

		tipNumX = 10;
		tipNumY = 10;

		layer = 1;
		layerSum = 1;
		selectedTip = 1;
	}

	public void setTipSize(int x, int y){
		tipSizeX = x;
		tipSizeY = y;
	}

	public void setMapSize(int x, int y){
		mapSizeX = x;
		mapSizeY = y;
	}

	public void setTipNum(int x, int y){
		tipNumX = x;
		tipNumY = y;
	}

	public void setLayer(int i){
		layer = i;
	}

	public void setSelectedTip(int i){
		selectedTip = i;
	}
}

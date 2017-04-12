package com.xqs.mypaoku;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.xqs.mypaoku.res.Res;
import com.xqs.mypaoku.screen.GameScreen;
import com.xqs.mypaoku.screen.LevelScreen;
import com.xqs.mypaoku.screen.MenuScreen;
import com.xqs.mypaoku.screen.SplashScreen;

public class MyPaokuGame extends Game {

	public static final String TAG = "MyPaokuGame";

	/** 是否显示帧率 */
	public static final boolean SHOW_FPS = true;

	/** 世界宽度 */
	private float worldWidth;
	/** 世界高度 */
	private float worldHeight;

	/** 资源管理器 */
	private AssetManager assetManager;

	/** 纹理图集 */
	private TextureAtlas atlas;

	/** 场景 */
	private SplashScreen splashScreen;
	private MenuScreen menuScreen;
	private LevelScreen levelScreen;
	private GameScreen gameScreen;


	/** 显示帧率所需要的位图字体 */
	private BitmapFont fpsBitmapFont;
	/** 用于调试显示帧率 */
	private FPSDebug fpsDebug;

	@Override
	public void create() {
		// 设置 LOG 输出级别
		Gdx.app.setLogLevel(Application.LOG_DEBUG);



		float graWidth=Gdx.graphics.getWidth();
		float graHeight=Gdx.graphics.getHeight();

		float ratio=graWidth/graHeight;

		// 为了不压扁或拉长图片, 按实际屏幕比例计算世界宽高
		worldHeight = Res.FIX_WORLD_HEIGHT;
		worldWidth = Res.FIX_WORLD_WIDTH;

        //取整
		worldWidth=(int)worldWidth;


		Gdx.app.log(TAG, "World Size: " + worldWidth + " * " + worldHeight);

		Gdx.app.log(TAG, "graphics Size: " + Gdx.graphics.getWidth()+ " * " + Gdx.graphics.getHeight());

		// 创建资源管理器
		assetManager = new AssetManager();

		// 加载资源
		assetManager.load(Res.Atlas.ATLAS_PATH, TextureAtlas.class);

		assetManager.load(Res.FPS_BITMAP_FONT_PATH, BitmapFont.class);


		// 等待资源加载完毕
		assetManager.finishLoading();

		// 获取资源
		atlas = assetManager.get(Res.Atlas.ATLAS_PATH, TextureAtlas.class);
		fpsBitmapFont = assetManager.get(Res.FPS_BITMAP_FONT_PATH, BitmapFont.class);


		// --- 场景 ---
		splashScreen = new SplashScreen(this);
		menuScreen = new MenuScreen(this);
		levelScreen = new LevelScreen(this);
		gameScreen = new GameScreen(this);

		// 设置当前场景
		setScreen(splashScreen);


		// 判断是否需要显示帧率, 如果需要, 则进行初始化
		if (SHOW_FPS) {
			fpsDebug = new FPSDebug();
			fpsDebug.init(fpsBitmapFont, 24);
		}
	}


	public void showMenuScreen(){
		menuScreen.init();
		setScreen(menuScreen);
		// free resources
		if(splashScreen!=null){
			splashScreen.dispose();
			splashScreen=null;
		}
	}

	public void showLevelScreen(){
		levelScreen.init();
		setScreen(levelScreen);
	}

	public void showGameScreen(){
		gameScreen.init();
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		// 黑色清屏
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// 父类渲染场景
		super.render();

		// 判断是否需要渲染帧率
		if (SHOW_FPS) {
			fpsDebug.render();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		// 应用退出时需要手动销毁场景
		if(menuScreen != null){
			menuScreen.dispose();
		}
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		// 应用退出时释放资源
		if (assetManager != null) {
			assetManager.dispose();
		}
		if (SHOW_FPS) {
			fpsDebug.dispose();
		}
	}

	public float getWorldWidth() {
		return worldWidth;
	}

	public float getWorldHeight() {
		return worldHeight;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public BitmapFont getFpsBitmapFont(){
		return fpsBitmapFont;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	/**
	 * 用于调试显示帧率
	 */
	private class FPSDebug implements Disposable {

		private SpriteBatch batch;

		private BitmapFont fpsBitmapFont;

		/** 文本高度占屏幕高度的比例 */
		private static final float OCCUPY_HEIGHT_RATIO = 14.0F / 480.0F;

		/** 显示的文本偏移右下角的X轴和Y轴比例(相对于字体高度的比例) */
		private static final float DISPLAY_ORIGIN_OFFSET_RATIO = 0.5F;

		// 帧率字体绘制的位置
		private float x;
		private float y;

		public void init(BitmapFont fpsBitmapFont, int fontRawPixSize) {
			this.batch = new SpriteBatch();
			this.fpsBitmapFont = fpsBitmapFont;
			// 计算帧率文本显示位置（为了兼容所有不同尺寸的屏幕）
			float height = Gdx.graphics.getHeight();
			float scale = (height * OCCUPY_HEIGHT_RATIO) / (float) fontRawPixSize;
			this.fpsBitmapFont.getData().setScale(scale);
			float scaledFontSize = fontRawPixSize * scale;
			float offset = scaledFontSize * DISPLAY_ORIGIN_OFFSET_RATIO;
			x = scaledFontSize - offset;
			y = scaledFontSize * 1.85F - offset;
		}

		public void render() {
			// 绘制帧率
			if(batch!=null) {
				batch.begin();
				fpsBitmapFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), x, y);
				batch.end();
			}
		}

		@Override
		public void dispose() {
			// 销毁 batch
			if (batch != null) {
				batch.dispose();
				batch = null;
			}

			// fpsBitmapFont 由资源管理器负责管理, 这里不需要销毁
		}
	}

}


package com.dualdigital.cupidshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class TheGame extends ApplicationAdapter {
	public static final int HEIGHT = 1440;
	public static final int WIDTH = 2400;
	private static GameStateManager gsm;
	private SpriteBatch batch;
	/*public static ActionResolver resolver;
	public static AdsController adsControl;
	public static ActivityMethods activityMethods;*/
	
	@Override
	public void create () {
		gsm = new GameStateManager();
		batch = new SpriteBatch();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AssetLoader.load();
			}
		}).start();
		gsm.push(new SplashScreen(gsm));
		new Thread(new Runnable() {
			@Override
			public void run() {
				Timer.schedule(new Timer.Task() {
					@Override
					public void run() {
						gsm.push(new Menu(gsm));
					}
				}, 3);
			}
		}).start();
	}

	public static boolean isGameOn(){
		if(gsm.top() instanceof PlayGame)
			return true;
		return false;
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void pause() {
		super.pause();
		if(isGameOn())
			gsm.push(new PauseScreen(gsm));
	}

	@Override
	public void resume() {
		super.resume();
	}
}

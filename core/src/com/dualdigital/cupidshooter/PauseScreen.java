package com.dualdigital.cupidshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class PauseScreen extends State {
    private Texture background;
    private FallingObject fallingObject;
    private Trolley trolley;
    private BitmapFont font;
    private BitmapFont scorefont;
    private BitmapFont shadow;
    private Label.LabelStyle labelStyle;
    int cameraWidth = TheGame.WIDTH / 2;
    int cameraHeight = TheGame.HEIGHT / 2;
    long score;
    Stage stage;
    private Label scoreBoard;

    protected PauseScreen(GameStateManager gcm) {
        super(gcm);
        /*if(TheGame.adsControl.isWifiConnected())
            TheGame.adsControl.showBannerAd();*/
        trolley = new Trolley(AssetLoader.trolley, new Vector3(PlayGame.trolleyX, PlayGame.trolleyY, 0));
        background = AssetLoader.background;
        camera.setToOrtho(false, TheGame.WIDTH / 2, TheGame.HEIGHT / 2);
        font = AssetLoader.font;
        font.getData().setScale(1.2f, 1.2f);
        shadow = AssetLoader.shadow;
        shadow.getData().setScale(1.2f, 1.2f);
        scorefont = AssetLoader.scoreFont;
        scorefont.getData().setScale(0.6f, 0.6f);
        stage = new Stage();
        labelStyle = new Label.LabelStyle(scorefont, Color.WHITE);
        this.score = score;
        String scoreString = "Score: " + score + "\nHigh Score: " + AssetLoader.getHighScore();
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched())
            gcm.pop();
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(trolley.getTexture(), trolley.getPosition().x, trolley.getPosition().y);
        String scoreString = "PAUSED";
        shadow.draw(sb, scoreString, TheGame.WIDTH / 4 - scoreString.length() * 10, (TheGame.HEIGHT / 8) * 3);
        font.draw(sb, scoreString, TheGame.WIDTH / 4 - scoreString.length() * 10, (TheGame.HEIGHT / 8) * 3);
        sb.end();
        stage.getViewport().setCamera(camera);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

package com.dualdigital.cupidshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/**
 * Created by tunde_000 on 31/12/2015.
 */
public class Menu extends State{
    private Texture background;
    private ImageButton playButton,leaderBoardButton,rateButton, muteButton, shareButton;
    Stage stage;
    int cameraWidth = TheGame.WIDTH / 2;
    int cameraHeight = TheGame.HEIGHT / 2;
    private BitmapFont fontTitle;

    protected Menu(GameStateManager gcm) {
        super(gcm);
        camera.setToOrtho(false, TheGame.WIDTH / 2, TheGame.HEIGHT / 2);
        background = AssetLoader.background;
        fontTitle = AssetLoader.font;
        fontTitle.getData().setScale(1.2f, 1.2f);
        stage = new Stage();
        initializeButtons();
        stage.addActor(playButton);
        stage.addActor(shareButton);
        stage.addActor(muteButton);
        stage.addActor(rateButton);
        stage.addActor(leaderBoardButton);
        Gdx.input.setInputProcessor(stage);
        TheGame.activityMethods.showFbButton();
        //if(TheGame.adsControl.isWifiConnected())
        TheGame.adsControl.hideBannerAd();
    }

    public void initializeButtons(){
        playButton = new ImageButton(AssetLoader.playStyle);
        playButton.setPosition(cameraWidth / 6 * 2, cameraHeight / 2);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("Button Clicked");
                gcm.set(new PlayGame(gcm));
            }
        });

        shareButton = new ImageButton(AssetLoader.shareStyle);
        shareButton.setPosition(cameraWidth / 6 * 2, cameraHeight / 3 - 30);
        shareButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("Share Button Clicked");
                //TheGame.activityMethods.shareScore();
            }
        });

        if(!AssetLoader.isSoundOn()){muteButton = new ImageButton(AssetLoader.muteStyle);}
        else{muteButton = new ImageButton(AssetLoader.soundStyle);}
        muteButton.setPosition(cameraWidth - muteButton.getWidth() - 20, cameraHeight - muteButton.getHeight() - 20);
        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(AssetLoader.isSoundOn()){
                    AssetLoader.toggleSound(false);
                    muteButton.setStyle(AssetLoader.muteStyle);
                    System.out.println("Mute Clicked: Sound is off");
                }
                else{
                    AssetLoader.toggleSound(true);
                    muteButton.setStyle(AssetLoader.soundStyle);
                    System.out.println("Mute Clicked: Sound is on");
                }
            }
        });

        leaderBoardButton = new ImageButton(AssetLoader.scoreStyle);
        leaderBoardButton.setPosition((cameraWidth / 6) * 3 + 60, cameraHeight / 3 - 30);
        leaderBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("LeaderBoard: Button Clicked");
                if(TheGame.activityMethods.isLoggedInFB())
                    TheGame.activityMethods.startLeaderboardActivity();
            }
        });

        rateButton = new ImageButton(AssetLoader.rateStyle);
        rateButton.setPosition((cameraWidth / 6) * 3 + 60, cameraHeight / 2);
        rateButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("Rate: Button Clicked");
                Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.dualtech.fallingpresents.android");
            }
        });
    }

    @Override
    protected void handleInput() {}

    @Override
    public void update(float dt) {
        handleInput();
        if(!TheGame.activityMethods.isLoggedInFB())
            leaderBoardButton.setTouchable(Touchable.disabled);
        else
            leaderBoardButton.setTouchable(Touchable.enabled);

        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
        String title = "MAIN MENU";
        fontTitle.draw(sb, title, TheGame.WIDTH / 4 - (title.length() * 35), (TheGame.HEIGHT / 8) * 3 + 30);
        sb.end();
        stage.getViewport().setCamera(camera);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

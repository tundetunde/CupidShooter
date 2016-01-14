package com.dualdigital.cupidshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class EndGame extends State {
    private Texture background;
    private FallingObject fallingObject;
    private Trolley trolley;
    private BitmapFont font, scorefont, shadow;
    private Label.LabelStyle labelStyle;
    private ImageButton playButton,leaderBoardButton,rateButton, muteButton, shareButton;
    int cameraWidth = TheGame.WIDTH / 2;
    int cameraHeight = TheGame.HEIGHT / 2;
    long score;
    Stage stage;
    private Label scoreBoard;

    protected EndGame(GameStateManager gcm, Vector3 presentPosition, Vector3 trolleyPosition, long score) {
        super(gcm);
        /*if(TheGame.adsControl.isWifiConnected())
            TheGame.adsControl.showBannerAd();*/
        fallingObject = new FallingObject(AssetLoader.christmasPresent , new Vector3((int)presentPosition.x, (int)presentPosition.y,0));
        trolley = new Trolley(AssetLoader.trolley, new Vector3(trolleyPosition.x, trolleyPosition.y ,0));
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
        initializeButtons();
        this.score = score;
        String scoreString = "Score: " + score + "\nHigh Score: " + AssetLoader.getHighScore();
        scoreBoard = new Label(scoreString, labelStyle);
        scoreBoard.setPosition((cameraWidth / 2) - (scoreBoard.getWidth() / 2), cameraHeight / 2 + 30);
        //scoreBoard.setSize(300, 500);
        stage.addActor(scoreBoard);
        stage.addActor(playButton);
        stage.addActor(shareButton);
        stage.addActor(muteButton);
        stage.addActor(rateButton);
        stage.addActor(leaderBoardButton);
        Gdx.input.setInputProcessor(stage);
    }

    public void initializeButtons(){
        playButton = new ImageButton(AssetLoader.playStyle);
        playButton.setPosition(cameraWidth / 6 * 2, cameraHeight / 3 + 20);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("Button Clicked");
                /*if(TheGame.adsControl.isWifiConnected())
                    TheGame.adsControl.hideBannerAd();*/
                gcm.set(new PlayGame(gcm));
            }
        });

        shareButton = new ImageButton(AssetLoader.shareStyle);
        shareButton.setPosition(cameraWidth / 6 * 2, cameraHeight / 4 - 40);
        shareButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("Share Button Clicked");
                /*if (TheGame.adsControl.isWifiConnected())
                    TheGame.adsControl.hideBannerAd();*/
                TheGame.activityMethods.shareScore(score);
            }
        });

        if(AssetLoader.isSoundOn()){muteButton = new ImageButton(AssetLoader.soundStyle);}
        else{muteButton = new ImageButton(AssetLoader.muteStyle);}
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
        leaderBoardButton.setPosition((cameraWidth / 6) * 3 + 60, cameraHeight / 4 - 40);
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
        rateButton.setPosition((cameraWidth / 6) * 3 + 60, cameraHeight / 3 + 20);
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
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(fallingObject.getTexture(), fallingObject.getPosition().x, fallingObject.getPosition().y);
        sb.draw(trolley.getTexture(), trolley.getPosition().x, trolley.getPosition().y);
        String over = "GAME OVER";
        shadow.draw(sb, over, (TheGame.WIDTH / 4) - (over.length() * 27), (cameraHeight / 10) * 9);
        font.draw(sb, over, (TheGame.WIDTH / 4) - (over.length() * 27), (cameraHeight / 10) * 9);
        sb.end();
        stage.getViewport().setCamera(camera);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

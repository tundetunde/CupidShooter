package com.dualdigital.cupidshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class EndGame extends State {
    private Texture background;
    private FallingObject fallingObject;
    private Shooter shooter;
    private BitmapFont font, scorefont;
    private Label.LabelStyle labelStyle;
    private ImageButton playButton,leaderBoardButton,rateButton, muteButton, shareButton;
    int cameraWidth = TheGame.WIDTH / 2;
    int cameraHeight = TheGame.HEIGHT / 2;
    long score;
    Stage stage;
    private Label scoreBoard;
    boolean isLeaderboardOn;

    protected EndGame(GameStateManager gcm, Vector3 presentPosition, Vector3 shooterPosition, long score) {
        super(gcm);
        //if(TheGame.adsControl.isWifiConnected())
        TheGame.adsControl.showBannerAd();
        TheGame.activityMethods.showFbButton();
        fallingObject = new FallingObject(PlayGame.heart , new Vector3((int)presentPosition.x, (int)presentPosition.y,0));
        shooter = new Shooter(AssetLoader.shooter, new Vector3(shooterPosition.x, shooterPosition.y ,0));
        background = AssetLoader.background;
        camera.setToOrtho(false, TheGame.WIDTH / 2, TheGame.HEIGHT / 2);
        font = AssetLoader.font;
        font.getData().setScale(1.2f, 1.2f);
        scorefont = AssetLoader.scoreFont;
        scorefont.getData().setScale(0.8f, 0.8f);
        stage = new Stage();
        labelStyle = new Label.LabelStyle(scorefont, Color.WHITE);
        initializeButtons();
        this.score = score;
        String scoreString = "Score: " + score + "\nHigh Score: " + AssetLoader.getHighScore();
        scoreBoard = new Label(scoreString, labelStyle);
        scoreBoard.setPosition((cameraWidth / 10) - 80, cameraHeight / 4 + 90);
        //scoreBoard.setSize(300, 500);
        stage.addActor(scoreBoard);
        stage.addActor(playButton);
        stage.addActor(shareButton);
        stage.addActor(muteButton);
        stage.addActor(rateButton);
        if(TheGame.activityMethods.isLoggedInFB()){
            stage.addActor(leaderBoardButton);
            isLeaderboardOn = true;
        }else{
            isLeaderboardOn = false;
        }
        Gdx.input.setInputProcessor(stage);
    }

    public void initializeButtons(){
        playButton = new ImageButton(AssetLoader.playStyle);
        playButton.setPosition(cameraWidth / 6 * 2, cameraHeight / 2);
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
        shareButton.setPosition(cameraWidth / 6 * 2, cameraHeight / 3 - 30);
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
                Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.dualdigital.cupidshooter");
            }
        });
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if(TheGame.activityMethods.isLoggedInFB()){
            if(!isLeaderboardOn){
                stage.addActor(leaderBoardButton);
                isLeaderboardOn = true;
            }
        }else {
            if(isLeaderboardOn){
                leaderBoardButton.remove();
                isLeaderboardOn = false;
            }
        }
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(fallingObject.getTexture(), fallingObject.getPosition().x, fallingObject.getPosition().y);
        sb.draw(shooter.getTexture(), shooter.getPosition().x, shooter.getPosition().y);
        String over = "GAME OVER";
        font.draw(sb, over, (TheGame.WIDTH / 4) - (over.length() * 40), (cameraHeight / 10) * 8);
        sb.end();
        stage.getViewport().setCamera(camera);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

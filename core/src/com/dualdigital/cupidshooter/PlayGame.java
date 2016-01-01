package com.dualdigital.cupidshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import org.omg.IOP.TAG_JAVA_CODEBASE;

import java.util.Random;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class PlayGame extends State {
    private FallingObject fallingObject;
    private Trolley trolley;
    private Texture background;
    private Random rand;
    private static long score;
    private static long lives;
    private BitmapFont font;
    private BitmapFont livesText;
    private BitmapFont shadow;
    int cameraWidth = TheGame.WIDTH / 2;
    int cameraHeight = TheGame.HEIGHT / 2;
    Stage stage = new Stage();
    public static int trolleyX, trolleyY;
    private Label.LabelStyle labelStyle;
    private Label instructions;
    private BitmapFont scorefont;
    private Arrow arrow;

    protected PlayGame(GameStateManager gcm) {
        super(gcm);
        lives = 3;
        rand = new Random();
        AssetLoader.setMotionControl(true);
        fallingObject = new FallingObject(AssetLoader.christmasPresent ,new Vector3(rand.nextInt(cameraWidth), cameraHeight,0));
        trolley = new Trolley(AssetLoader.trolley, new Vector3(cameraWidth / 2, 0 ,0));
        background = AssetLoader.background;
        camera.setToOrtho(false, TheGame.WIDTH / 2, TheGame.HEIGHT / 2);
        score = 0;
        livesText = AssetLoader.livesLeft;
        livesText.getData().setScale(0.7f, 0.7f);
        font = AssetLoader.font;
        font.getData().setScale(1.2f, 1.2f);
        shadow = AssetLoader.shadow;
        shadow.getData().setScale(1.2f, 1.2f);
        scorefont = AssetLoader.scoreFont;
        scorefont.getData().setScale(0.6f, 0.6f);
        labelStyle = new Label.LabelStyle(scorefont, Color.PURPLE);
        String instructionsText = "Tilt the screen left or right to move the trolley\nTap the screen to shoot arrow\nTo Play, Tap Screen!";
        instructions = new Label(instructionsText, labelStyle);
        instructions.setPosition((cameraWidth / 2) - (instructions.getWidth() / 2), cameraHeight / 2 - 40);
        stage.addActor(instructions);
        arrow = new Arrow(AssetLoader.arrow, new Vector3(trolleyX, trolleyY + trolley.getTexture().getHeight(), 0), new Vector3(0, 0, 0));
        //FallingPresentsGame.activityMethods.hideFbButton();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        float y = Gdx.input.getAccelerometerY();
        trolley.move(y);

        //If user has touched screen, shoot arrow
        if(Gdx.input.justTouched())
            arrow.setShoot(true, y);
    }

    @Override
    public void update(float dt) {
        trolleyX = (int)trolley.getPosition().x;
        trolleyY = (int)trolley.getPosition().y;
        if(TheGame.isGameOn()){
            if(AssetLoader.isFirstTime()){
                if(Gdx.input.justTouched()){
                    AssetLoader.setFirstTime(false);
                    instructions.setVisible(false);
                }
            }else{
                instructions.setVisible(false);
                handleInput();
                fallingObject.update(dt);

                //Check if arrow hits object then add to score
                if(checkHit()){
                    score++;
                    if(AssetLoader.isSoundOn())
                        AssetLoader.coin.play();
                    fallingObject = new FallingObject(AssetLoader.christmasPresent ,new Vector3(rand.nextInt(cameraWidth), cameraHeight,0));
                    arrow = new Arrow(AssetLoader.arrow, new Vector3(trolleyX, trolleyY, 0), new Vector3(trolley.velocity.x, trolley.velocity.y, 0));
                }

                //Check if arrow has gone off screen
                if(arrow.isArrowOutOfBounds())
                    arrow = new Arrow(AssetLoader.arrow, new Vector3(trolleyX, trolleyY, 0), new Vector3(trolley.velocity.x, trolley.velocity.y, 0));

                if(fallingObject.isHitGround() || trolley.isCollide(fallingObject.getBounds())){
                    lives--;
                    System.out.println("LIVES LEFT: " + lives);
                    if(lives == 0){
                        if (score > AssetLoader.getHighScore()) {
                            AssetLoader.setHighScore(score);
                        /*if(FallingPresentsGame.activityMethods.isLoggedInFB()){
                            FallingPresentsGame.activityMethods.postFacebookScore(score);
                            FallingPresentsGame.activityMethods.postLeaderboard();
                        }*/
                        /*FallingPresentsGame.activityMethods.postFacebookScore(score);
                        FallingPresentsGame.activityMethods.postLeaderboard();*/
                        }
                        gcm.set(new EndGame(gcm, fallingObject.getPosition(), trolley.getPosition(), score));
                    }else{
                        fallingObject = new FallingObject(AssetLoader.christmasPresent ,new Vector3(rand.nextInt(cameraWidth), cameraHeight,0));
                    }
                }

                trolley.update(dt);
                arrow.update(dt, trolleyX + (trolley.getTexture().getWidth() / 2) - (arrow.getTexture().getWidth() / 2), trolleyY + (trolley.getTexture().getHeight() - 30), trolley.velocity.x);
            }
        }else{
            if(Gdx.input.justTouched()){
                fallingObject = new FallingObject(AssetLoader.christmasPresent ,new Vector3(rand.nextInt(cameraWidth), cameraHeight,0));
            }
        }
    }

    public boolean checkHit(){
        if(arrow.isCollide(fallingObject.getBounds())){
            return true;
        }
        return false;
    }

    @Override
    public void render(SpriteBatch sb) {
        if(TheGame.isGameOn()){
            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
            sb.draw(fallingObject.getTexture(), fallingObject.getPosition().x, fallingObject.getPosition().y);
            sb.draw(trolley.getTexture(), trolley.getPosition().x, trolley.getPosition().y);
            //sb.draw(arrow.getTexture(), arrow.getPosition().x, arrow.getPosition().y);
            sb.draw(arrow.arrowSprite, arrow.getPosition().x, arrow.getPosition().y);
            String scoreString = Long.toString(score);
            String livesLeft = "Lives: " + Long.toString(lives);
            livesText.draw(sb, livesLeft, 0, TheGame.HEIGHT / 2 - 30);
            shadow.draw(sb, scoreString, TheGame.WIDTH / 4 - scoreString.length() * 10, (TheGame.HEIGHT / 8) * 3);
            font.draw(sb, scoreString, TheGame.WIDTH / 4 - scoreString.length() * 10, (TheGame.HEIGHT / 8) * 3);
            sb.end();
            stage.getViewport().setCamera(camera);
            stage.draw();
        }else{
            sb.begin();
            sb.draw(background, camera.position.x - (camera.viewportWidth / 2), 0);
            sb.draw(fallingObject.getTexture(), fallingObject.getPosition().x, fallingObject.getPosition().y);
            sb.draw(trolley.getTexture(), trolley.getPosition().x, trolley.getPosition().y);
            String h = "TAP TO CONTINUE";
            shadow.draw(sb, h, TheGame.WIDTH / 4 - h.length() * 10, (TheGame.HEIGHT / 8) * 3);
            font.draw(sb, h, TheGame.WIDTH / 4 - h.length() * 10, (TheGame.HEIGHT / 8) * 3);
            sb.end();
        }
    }

    @Override
    public void dispose() {

    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}

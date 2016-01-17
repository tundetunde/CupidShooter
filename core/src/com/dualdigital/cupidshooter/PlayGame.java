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

import java.util.ArrayList;
import java.util.Random;

public class PlayGame extends State {
    ArrayList<FallingObject> aliveFallingObjects;
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
    private ArrayList<FallingObject> fallingObjects;
    static Texture heart;

    protected PlayGame(GameStateManager gcm) {
        super(gcm);
        lives = 3;
        rand = new Random();
        AssetLoader.setMotionControl(true);
        trolley = new Trolley(AssetLoader.trolley, new Vector3(0, 0 ,0));
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
        arrow = new Arrow(AssetLoader.arrow, new Vector3(trolleyX, trolleyY + (trolley.getTexture().getHeight() / 2), 0), new Vector3(0, 0, 0));
        fallingObjects = new ArrayList<FallingObject>();
        addObject();
        TheGame.activityMethods.hideFbButton();
        if(TheGame.adsControl.isWifiConnected())
            TheGame.adsControl.hideBannerAd();
        Gdx.input.setInputProcessor(stage);
    }

    public void addObject(){
        int hnum = new Random().nextInt(6)+1;
        if(hnum==1){heart=AssetLoader.h1;}
        else if(hnum==2){heart=AssetLoader.h2;}
        else if(hnum==3){heart=AssetLoader.h3;}
        else if(hnum==4){heart=AssetLoader.h4;}
        else if(hnum==5){heart=AssetLoader.h5;}
        else {heart=AssetLoader.h6;}
        /*Random rand = new Random();
        int objectInARow = rand.nextInt(3);*/
        fallingObjects.add(new FallingObject(heart ,new Vector3(rand.nextInt(cameraWidth - heart.getWidth()), cameraHeight,0)));
        /*if(objectInARow == 0)
            fallingObjects.add(new FallingObject(AssetLoader.christmasPresent ,new Vector3(rand.nextInt(cameraWidth - AssetLoader.christmasPresent.getWidth()), cameraHeight,0)));
        else if(objectInARow == 1) {
            int x1 = rand.nextInt(cameraWidth - (AssetLoader.christmasPresent.getWidth() * 2));
            fallingObjects.add(new FallingObject(AssetLoader.christmasPresent, new Vector3(x1, cameraHeight, 0)));
            fallingObjects.add(new FallingObject(AssetLoader.christmasPresent, new Vector3(x1 + AssetLoader.christmasPresent.getWidth() + 30, cameraHeight, 0)));
        }else if(objectInARow == 2){
            int x1 = rand.nextInt(cameraWidth - (AssetLoader.christmasPresent.getWidth() * 3));
            int x2 = x1 + AssetLoader.christmasPresent.getWidth() + 30;
            fallingObjects.add(new FallingObject(AssetLoader.christmasPresent ,new Vector3(x1, cameraHeight,0)));
            fallingObjects.add(new FallingObject(AssetLoader.christmasPresent ,new Vector3(x2, cameraHeight,0)));
            fallingObjects.add(new FallingObject(AssetLoader.christmasPresent ,new Vector3(x2 + AssetLoader.christmasPresent.getWidth() + 30, cameraHeight,0)));*/
        //}
    }

    @Override
    protected void handleInput() {
        float x = Gdx.input.getAccelerometerX();
        trolley.move(-x);

        //If user has touched screen, shoot arrow
        if(Gdx.input.justTouched())
            arrow.setShoot(true, x);
    }

    @Override
    public void update(float dt) {
        boolean newObjectNeeded = false;
        trolleyX = (int)trolley.getPosition().x;
        trolleyY = (int)trolley.getPosition().y;
        ArrayList<FallingObject> tempFallingObjects = new ArrayList<FallingObject>();
        tempFallingObjects.addAll(fallingObjects);
        if(TheGame.isGameOn()){
            if(AssetLoader.isFirstTime()){
                if(Gdx.input.justTouched()){
                    AssetLoader.setFirstTime(false);
                    instructions.setVisible(false);
                }
            }else{
                instructions.setVisible(false);
                handleInput();

                //Check if arrow has gone off screen
                if(arrow.isArrowOutOfBounds())
                    arrow = new Arrow(AssetLoader.arrow, new Vector3(trolleyX, trolleyY + (trolley.getTexture().getHeight() / 2), 0), new Vector3(trolley.velocity.x, trolley.velocity.y, 0));

                for(FallingObject y : tempFallingObjects){
                    y.update(dt);
                    if(checkHit(y)){
                        /*if(!y.isDead()){
                            y.hit();
                            arrow = new Arrow(AssetLoader.arrow, new Vector3(trolleyX, trolleyY + (trolley.getTexture().getHeight() / 2), 0), new Vector3(trolley.velocity.x, trolley.velocity.y, 0));
                        }*/
                        y.hit();
                        score++;
                        if(AssetLoader.isSoundOn())
                            AssetLoader.coin.play();
                        if(tempFallingObjects.size() == 1)
                            newObjectNeeded = true;
                        //newObjectNeeded = true;
                    }
                    if(y.isHitGround() || trolley.isCollide(y.getBounds())){
                        lives--;
                        y.hit();
                        System.out.println("LIVES LEFT: " + lives);
                        if(lives == 0){
                            if (score > AssetLoader.getHighScore()) {
                                AssetLoader.setHighScore(score);
                                if(TheGame.activityMethods.isLoggedInFB()){
                                    TheGame.activityMethods.postFacebookScore(score);
                                }
                            }
                            gcm.set(new EndGame(gcm, y.getPosition(), trolley.getPosition(), score));
                        }else{
                            newObjectNeeded = true;
                        }
                    }
                    if(y.isHitGround()){
                        newObjectNeeded = true;
                    }
                }

                trolley.update(dt);
                arrow.update(dt, trolleyX + (trolley.getTexture().getWidth() / 2) - (arrow.getTexture().getWidth() / 2), trolleyY + (trolley.getTexture().getHeight() / 2), trolley.velocity.x);
                aliveFallingObjects = new ArrayList<FallingObject>();
                for(FallingObject x : tempFallingObjects){
                    if(!x.isDead())
                        aliveFallingObjects.add(x);
                }
                fallingObjects.clear();
                if(newObjectNeeded)
                    addObject();
                fallingObjects.addAll(aliveFallingObjects);
                //newObjectPerSecond();
            }
        }else{
            if (Gdx.input.justTouched()){
                addObject();
            }
        }
    }

    public boolean checkHit(FallingObject x){
        if(arrow.isCollide(x.getBounds())){
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
            for(FallingObject x: fallingObjects){
                sb.draw(x.getTexture(), x.getPosition().x, x.getPosition().y);
            }
            sb.draw(trolley.getTexture(), trolley.getPosition().x, trolley.getPosition().y);
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
            for(FallingObject x: fallingObjects){
                sb.draw(x.getTexture(), x.getPosition().x, x.getPosition().y);
            }

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

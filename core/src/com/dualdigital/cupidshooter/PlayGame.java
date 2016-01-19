package com.dualdigital.cupidshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.Random;

public class PlayGame extends State {
    private Shooter shooter;
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
    public static int shooterX, shooterY, arrowX;
    private Label.LabelStyle labelStyle;
    private Label instructions;
    private BitmapFont scorefont;
    //private Arrow arrow;
    private ArrayList<Arrow> arrowList;
    private ArrayList<FallingObject> fallingObjects;
    static Texture heart;
    private float timer, lastTimer = 0;
    private double timeIntervals = 1.0;

    protected PlayGame(GameStateManager gcm) {
        super(gcm);
        lives = 3;
        rand = new Random();
        AssetLoader.setMotionControl(true);
        shooter = new Shooter(AssetLoader.shooter, new Vector3(0, 0 ,0));
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
        String instructionsText = "Tilt the screen left or right to move the shooter\nTap the screen to shoot arrow\nTo Play, Tap Screen!";
        instructions = new Label(instructionsText, labelStyle);
        instructions.setPosition((cameraWidth / 2) - (instructions.getWidth() / 2), cameraHeight / 2 - 40);
        stage.addActor(instructions);
        arrowList = new ArrayList<Arrow>();
        arrowList.add(new Arrow(AssetLoader.arrow, new Vector3(shooterX, shooterY + (shooter.getTexture().getHeight() / 2), 0), new Vector3(0, 0, 0)));
        //arrow = new Arrow(AssetLoader.arrow, new Vector3(shooterX, shooterY + (shooter.getTexture().getHeight() / 2), 0), new Vector3(0, 0, 0));
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
        fallingObjects.add(new FallingObject(heart ,new Vector3(rand.nextInt(cameraWidth - heart.getWidth() - shooter.getTexture().getWidth()) + shooter.getTexture().getWidth() + AssetLoader.arrow.getWidth(), cameraHeight,0)));
    }

    @Override
    protected void handleInput() {
        float x = Gdx.input.getAccelerometerX();
        shooter.move(-x);

        //If user has touched screen, shoot arrow
        if(Gdx.input.justTouched()){
            arrowList.get(arrowList.size() - 1).setShoot(true, x);
            arrowList.add(new Arrow(AssetLoader.arrow, new Vector3(shooterX, shooterY + (shooter.getTexture().getHeight() / 2), 0), new Vector3(0, 0, 0)));
        }
            //arrow.setShoot(true, x);
    }

    @Override
    public void update(float dt) {
        Random randTime = new Random();
        shooterX = (int)shooter.getPosition().x;
        shooterY = (int)shooter.getPosition().y;
        if(TheGame.isGameOn()){
            //if the game is active atm ...enter here
            if(AssetLoader.isFirstTime()){
                if(Gdx.input.justTouched()){
                    AssetLoader.setFirstTime(false);
                    instructions.setVisible(false);
                }
            }else{
                instructions.setVisible(false);
                handleInput();
                //times the object to drop every second
                timer += Gdx.graphics.getDeltaTime();
                if(timer-lastTimer > timeIntervals){
                    addObject();
                    lastTimer = timer;
                    timeIntervals = (randTime.nextInt(31) / 10) + 0.5;
                }

                //Check if arrow has gone off screen
                for(int j = 0; j < arrowList.size(); j++){
                    if(arrowList.get(j).isArrowOutOfBounds()){
                        arrowList.remove(j);
                    }
                }
                /*if(arrow.isArrowOutOfBounds())
                    arrow = new Arrow(AssetLoader.arrow, new Vector3(shooterX, shooterY + (shooter.getTexture().getHeight() / 2), 0), new Vector3(shooter.velocity.x, shooter.velocity.y, 0));*/


                for(int i = 0; i<fallingObjects.size(); i++){
                    FallingObject obj = fallingObjects.get(i);
                    obj.update(dt);
                    for(Arrow arrow: arrowList){
                        if(checkHit(obj, arrow)){
                            fallingObjects.remove(i);
                            score++;
                            if(AssetLoader.isSoundOn())
                                AssetLoader.coin.play();
                        }
                    }

                    if(obj.isHitGround() || shooter.isCollide(obj.getBounds())){
                        //if an object is missed and hits the ground
                        lives--;
                        fallingObjects.remove(i);
                        System.out.println("LIVES LEFT: " + lives);
                        if(lives == 0){
                            if (score > AssetLoader.getHighScore()) {
                                AssetLoader.setHighScore(score);
                                if(TheGame.activityMethods.isLoggedInFB()){
                                    TheGame.activityMethods.postFacebookScore(score);
                                }
                            }
                            gcm.set(new EndGame(gcm, obj.getPosition(), shooter.getPosition(), score));
                        }
                    }
                }

                shooter.update(dt);
                for(Arrow arrow: arrowList){
                    arrow.update(dt, shooterX + (shooter.getTexture().getWidth() / 2) - (arrow.getTexture().getWidth() / 2), shooterY + (shooter.getTexture().getHeight() / 2), shooter.velocity.x);
                }

            }
        }else{
            //to resume game from pause
            if (Gdx.input.justTouched()){
                addObject();
            }
        }
    }

    public boolean checkHit(FallingObject x, Arrow arrow){
        return arrow.isCollide(x.getBounds());
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
            for(Arrow arrow: arrowList){
                sb.draw(arrow.arrowSprite, arrow.getPosition().x, arrow.getPosition().y);
            }
            sb.draw(shooter.getTexture(), shooter.getPosition().x, shooter.getPosition().y);
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

            sb.draw(shooter.getTexture(), shooter.getPosition().x, shooter.getPosition().y);
            String h = "TAP TO CONTINUE";
            shadow.draw(sb, h, TheGame.WIDTH / 4 - h.length() * 10, (TheGame.HEIGHT / 8) * 3);
            font.draw(sb, h, TheGame.WIDTH / 4 - h.length() * 10, (TheGame.HEIGHT / 8) * 3);
            sb.end();
        }
    }

    @Override
    public void dispose() {

    }
}

package com.dualdigital.cupidshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class AssetLoader {
    public static Preferences prefs;
    public static Sound coin;
    public static BitmapFont font, shadow, scoreFont, livesLeft;
    public static Texture background,trolley, splash, christmasPresent, arrow, heart;
    public static Texture p1, p2, p3, p4, p5;
    public static TextureAtlas presentsAtlas;
    public static Skin menuSkin;
    public static ImageButton.ImageButtonStyle playStyle, rateStyle, scoreStyle, muteStyle, soundStyle, shareStyle;
    public static Label.LabelStyle labelStyle;

    public static void load(){
        prefs = Gdx.app.getPreferences("Falling Presents");
        font = new BitmapFont(Gdx.files.internal("text.fnt"));
        livesLeft = new BitmapFont(Gdx.files.internal("text.fnt"));
        shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        scoreFont = new BitmapFont(Gdx.files.internal("text.fnt"));
        background = new Texture("bg.png");
        splash = new Texture("splash.png");
        trolley = new Texture("cupid.png");
        arrow = new Texture("arrow.png");

        labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("text.fnt"));
        labelStyle.font.getData().setScale(0.5f, 0.5f);

        p1 = new Texture("present2.png");
        p2 = new Texture("present3.png");
        p3 = new Texture("present4.png");
        p4 = new Texture("present5.png");
        heart = new Texture("redbox.png");
        christmasPresent = p4;

        menuSkin= new Skin();
        menuSkin.addRegions(new TextureAtlas(Gdx.files.internal("menuButtons.pack")));
        playStyle = new ImageButton.ImageButtonStyle();
        playStyle.imageUp = menuSkin.getDrawable("play");
        playStyle.imageDown = menuSkin.getDrawable("playR");
        rateStyle = new ImageButton.ImageButtonStyle();
        rateStyle.imageUp = menuSkin.getDrawable("rate");
        rateStyle.imageDown = menuSkin.getDrawable("rateR");
        scoreStyle = new ImageButton.ImageButtonStyle();
        scoreStyle.imageUp = menuSkin.getDrawable("score");
        scoreStyle.imageDown = menuSkin.getDrawable("scoreR");
        muteStyle = new ImageButton.ImageButtonStyle();
        muteStyle.imageUp = menuSkin.getDrawable("mute");
        soundStyle = new ImageButton.ImageButtonStyle();
        soundStyle.imageUp = menuSkin.getDrawable("sound");
        shareStyle = new ImageButton.ImageButtonStyle();
        shareStyle.imageUp = menuSkin.getDrawable("share");
        shareStyle.imageDown = menuSkin.getDrawable("shareR");

        // Provide default high score of 0
        if (!prefs.contains("highScore")) {
            prefs.putLong("highScore", 0);
        }

        if (!prefs.contains("firstTime")) {
            prefs.putBoolean("firstTime", true);
        }

        if (!prefs.contains("motionControl")) {
            prefs.putBoolean("motionControl", true);
        }

        if (!prefs.contains("soundControl")) {
            prefs.putBoolean("soundControl", true);
        }
        coin = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
    }

    public Texture getBackground(){
        return background;
    }

    // Receives an integer and maps it to the String highScore in prefs
    public static void setHighScore(long val) {
        prefs.putLong("highScore", val);
        prefs.flush();
    }

    // Retrieves the current high score
    public static long getHighScore() {
        return prefs.getLong("highScore");
    }

    public static void setFirstTime(boolean val) {
        prefs.putBoolean("firstTime", val);
        prefs.flush();
    }

    public static boolean isFirstTime() {
        return prefs.getBoolean("firstTime");
    }

    public static void toggleSound(boolean val) {
        prefs.putBoolean("soundControl", val);
        prefs.flush();
    }

    public static boolean isSoundOn() {
        return prefs.getBoolean("soundControl",true);
    }

    // Receives an integer and maps it to the String highScore in prefs
    public static void setMotionControl(boolean val) {
        prefs.putBoolean("motionControl", val);
        prefs.flush();
    }

    public static void dispose(){
        background.dispose();
        trolley.dispose();
        christmasPresent.dispose();
        font.dispose();
        shadow.dispose();
        scoreFont.dispose();
        p1.dispose();
        p2.dispose();
        p3.dispose();
        p4.dispose();
        p5.dispose();
        splash.dispose();
        menuSkin.dispose();
        coin.dispose();
        arrow.dispose();
    }
}


package com.dualdigital.cupidshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class Arrow extends GameObject {
    private boolean shoot;
    private float shootVelocityX; //Velocity when arrow is released
    public Sprite arrowSprite; // Used to display texture/graphic

    //Contructor
    public Arrow(Texture texture, Vector3 position, Vector3 velocity) {
        super(texture, position, velocity);
        shoot = false;
        arrowSprite = new Sprite(AssetLoader.arrow);
    }

    @Override
    public void update(float dt) {

    }

    public void update(float dt, float x, float y, float velocityX){
        //If it hasnt been shot, move with trolley
        if(!shoot){
            position.x = x;
            position.y = y;
            position.add(velocity.x, velocity.y, 0);
        }else{
            position.add(shootVelocityX * 6, velocity.y, 0);
        }
        rotate(); // Under Construction

        //Collision Detection
        bounds.setPosition(position.x, position.y);

        if(shoot)
            shoot();
    }

    //sets value to true if arrow is shot, sets velocity as well
    public void setShoot(boolean shoot, float velocityX){
        this.shoot = shoot;
        shootVelocityX = velocityX;
    }

    public void shoot(){
        velocity.y += 1;
        velocity.x += shootVelocityX;
    }

    //Ignore for now
    public void hit(){
        dead = true;
    }

    public void rotate(){
        arrowSprite.setRotation(shootVelocityX * 100);
    }

    public boolean isCollide(Rectangle present){
        return present.overlaps(bounds);
    }

    public boolean isArrowOutOfBounds(){
        if(position.y >= TheGame.HEIGHT / 2)
            return true;
        return false;
    }
}

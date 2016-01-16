package com.dualdigital.cupidshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class FallingObject extends GameObject{
    private static final int GRAVITY = -100;
    private boolean dead;

    public FallingObject(Texture texture, Vector3 position) {
        super(texture, position, new Vector3(0,0,0));
        if(position.x < TheGame.WIDTH / 2 - texture.getWidth()){
            //position = new Vector3(p, y, 0);
        }
        else{
            position.x = position.x - texture.getWidth();
        }
        dead = false;
    }

    public void hit(){
        dead = true;
    }

    public boolean isDead(){
        return dead;
    }

    public boolean isHitGround(){
        if(position.y == 0)
            return true;
        return false;
    }

    public void toHeart(){
        texture = AssetLoader.heart;
    }

    @Override
    public void update(float dt) {
        if(position.y > 0)
            velocity.y = GRAVITY;
        velocity.scl(dt);
        position.add(0, velocity.y, 0);
        bounds.setPosition(position.x, position.y);
        if(position.y < 0)
            position.y = 0;
        velocity.scl(1 / dt);
    }
}

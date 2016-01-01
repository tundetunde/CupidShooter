package com.dualdigital.cupidshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class FallingObject extends GameObject{
    private static final int GRAVITY = -7;

    public FallingObject(Texture texture, Vector3 position) {
        super(texture, position, new Vector3(0,0,0));
        if (position.x < TheGame.WIDTH / 2 - texture.getWidth()){
            //position = new Vector3(p, y, 0);
        }
        else{
            position.x = position.x - texture.getWidth();
        }
    }

    public boolean isHitGround(){
        if(position.y == 0)
            return true;
        return false;
    }

    @Override
    public void update(float dt) {
        if(position.y > 0)
            velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(0, velocity.y, 0);
        bounds.setPosition(position.x, position.y);
        if(position.y < 0)
            position.y = 0;
        velocity.scl(1 / dt);
    }
}

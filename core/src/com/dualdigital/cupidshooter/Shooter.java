package com.dualdigital.cupidshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class Shooter extends GameObject {

    public Shooter(Texture texture, Vector3 position) {
        super(texture, position, new Vector3(0,0,0));
    }

    @Override
    public void update(float dt) {
        if(position.y > 0 || position.y < ((TheGame.WIDTH / 2) - texture.getWidth()))
            position.add(0, velocity.y, 0);
        if(position.y < 0)
            position.y = 0;
        if(position.y > (TheGame.HEIGHT / 2) - texture.getHeight())
            position.y = TheGame.HEIGHT / 2 - texture.getHeight();
        bounds.setPosition(position.x, position.y);
        velocity.scl(1 / dt);
    }

    public void move(float y){
        velocity.y = (y * 3);
    }

    public boolean isCollide(Rectangle shooter){
        return shooter.overlaps(bounds);
    }
}

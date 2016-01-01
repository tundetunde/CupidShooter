package com.dualdigital.cupidshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public class Trolley extends GameObject {

    public Trolley(Texture texture, Vector3 position) {
        super(texture, position, new Vector3(0,0,0));
    }

    @Override
    public void update(float dt) {
        if(position.x > 0 || position.x < ((TheGame.WIDTH / 2) - texture.getWidth()))
            position.add(velocity.x, 0, 0);
        if(position.x < 0)
            position.x = 0;
        if(position.x > (TheGame.WIDTH / 2) - texture.getWidth())
            position.x = TheGame.WIDTH / 2 - texture.getWidth();
        bounds.setPosition(position.x, position.y);
        velocity.scl(1 / dt);
    }

    public void move(float x){
        velocity.x = (x * 3);
    }

    public boolean isCollide(Rectangle present){
        return present.overlaps(bounds);
    }
}

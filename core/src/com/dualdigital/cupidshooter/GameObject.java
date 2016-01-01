package com.dualdigital.cupidshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by tunde_000 on 31/12/2015.
 */
public abstract class GameObject {
    protected Vector3 position;
    protected Vector3 velocity;
    protected Texture texture;
    protected Rectangle bounds;
    protected boolean dead;
    static int numberOfObjects;

    public GameObject(Texture texture, Vector3 position, Vector3 velocity){
        this.position = position;
        this.velocity = velocity;
        this.texture = texture;
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight() - 40);
        numberOfObjects++;
        dead = false;
    }

    public abstract void update(float dt);

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose(){
        texture.dispose();
    }
}

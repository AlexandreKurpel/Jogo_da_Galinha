package com.mygdx.game.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Colisao;

public abstract class GenericController implements Colisao {
    private Texture texture;
    private float height, width, x, y, speed;



//    private Rectangle rectangle;

    public GenericController() {}

    public abstract void render(SpriteBatch batch);

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

//    public Rectangle getRectangle() {
//        return rectangle;
//    }
//
//    public void setRectangle(Rectangle rectangle) {
//        this.rectangle = rectangle;
//    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return width;
    }

    public boolean isColid() {
        return true;
    }


}

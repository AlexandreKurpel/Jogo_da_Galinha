package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Assets;

public class CoyoteController extends GenericController {

    public CoyoteController() {
        this.setTexture(Assets.manager.get(Assets.RAPOSO_INVERTIDO_TEXTURE));
        this.setX(2);
        this.setY(20);
        this.setWidth(Gdx.graphics.getHeight() / 5f);
        this.setHeight(Gdx.graphics.getHeight() / 5f);
        this.setSpeed(Gdx.graphics.getWidth() / 3f);

    }

    public void update(float deltaTime, boolean pressA , boolean pressD) {
        if(pressA){
            this.setTexture(Assets.manager.get(Assets.RAPOSO_TEXTURE));
            this.setX(this.getX() - this.getSpeed() * deltaTime);
        }else if(pressD){
            this.setTexture(Assets.manager.get(Assets.RAPOSO_INVERTIDO_TEXTURE));
            this.setX(this.getX() + this.getSpeed() * deltaTime);
        }

        if (getX() < 0){
            setX(0);
        }
        if (getX() > Gdx.graphics.getWidth() - getWidth()){
            setX(Gdx.graphics.getWidth() - getWidth());
        }


    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(getTexture(), getX(), getY(),getWidth(),getHeight());
    }

    @Override
    public void dispose() {
        super.dispose();
    }


}

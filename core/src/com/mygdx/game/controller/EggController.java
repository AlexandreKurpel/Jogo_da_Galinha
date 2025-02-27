package com.mygdx.game.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Assets;

public class EggController extends GenericController {

    private Sound eggCatchSound;

    private Boolean bomb = false;

    public EggController(float x, float y) {
        this.setX(x);
        this.setY(y);
        this.setHeight(Gdx.graphics.getHeight() / 25f);
        this.setWidth(Gdx.graphics.getHeight() / 25f);

        // 20% de chance de ser uma bomba
        bomb = MathUtils.random() < 0.2f;

        if (bomb) {
            this.setTexture(Assets.manager.get(Assets.BOMBA_TEXTURE));
            this.setEggCatchSound(Assets.manager.get(Assets.CATCH_BOMBA_SOUND));
        } else {
            this.setTexture(Assets.manager.get(Assets.OVO_TEXTURE));
            this.setEggCatchSound(Assets.manager.get(Assets.CATCH_EGG_SOUND));
        }

    }


    public Sound getEggCatchSound() {
        return eggCatchSound;
    }

    public void setEggCatchSound(Sound eggCatchSound) {
        this.eggCatchSound = eggCatchSound;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
    }

    public void update(float deltaTime) {
        setY(getY() - 200 * deltaTime);
    }

    public boolean isOutOfBounds() {
        return getY() + getHeight() < 0;
    }

    public Boolean getBomb() {
        return bomb;
    }

    public void setBomb(Boolean bomb) {
        this.bomb = bomb;
    }

    @Override
    public void dispose() {
        super.dispose();
    }


}

package com.mygdx.game.controller;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.Assets;

public class ChickenController extends GenericController {

    private Sprite sprite;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> movingAnimation;
    private float elapsedTime;
    private Animation<TextureRegion> currentAnimation;
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 5;
    private static final float FRAME_DURATION = 0.2f;

    private Array<EggController> eggs;

    public ChickenController() {
        this.setTexture(Assets.manager.get(Assets.GALINHA_TEXTURE));
        this.setY(Gdx.graphics.getHeight() - ((Gdx.graphics.getHeight() / 11f) * 2f));
        this.setHeight(Gdx.graphics.getHeight() / 11f);
        this.setWidth(Gdx.graphics.getHeight() / 11f);
        eggs = new Array<>();

    }

    public void shoot() {
        eggs.add(new EggController(this.getX() + (getWidth() / 2f), this.getY()));
    }

    public void render(SpriteBatch batch) {

        batch.draw(getTexture(), getX(), getY(),getWidth(),getHeight());

        for (EggController egg : this.getEggs()) {
            egg.render(batch);
        }

    }

    public Array<EggController> getEggs() {
        return eggs;
    }

    public void setEggs(Array<EggController> eggs) {
        this.eggs = eggs;
    }

    @Override
    public void dispose() {
        for (EggController egg : this.getEggs()) {
            egg.dispose();
        }

        super.dispose();
    }
}

package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;

public class CobraController extends GenericController {

    private Sprite sprite;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> movingAnimation;
    private Animation<TextureRegion> collisionAnimation;
    private Animation<TextureRegion> damageAnimation;
    private Animation<TextureRegion> deathAnimation;
    private float elapsedTime;
    private boolean isMoving;
    private boolean isColliding;
    private boolean isDamaged;
    private boolean isDead;
    private float collisionTime;
    public int damageCount;
    private Animation<TextureRegion> currentAnimation;
    private boolean deathAnimationStarted;

    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 5;
    private static final float FRAME_DURATION = 0.2f;
    private static final float COLLISION_DURATION = 1.0f;

    public CobraController() {
        this.setX(2);
        this.setY(20);
        this.setTexture(Assets.manager.get(Assets.SPRITCOBRA));
        sprite = new Sprite(this.getTexture(), 0, 0, 25, 25);
        sprite.setPosition(0, 10);
        sprite.setSize(Gdx.graphics.getHeight() / 5f, Gdx.graphics.getHeight() / 5f);
        this.setSpeed(Gdx.graphics.getWidth() / 4f);

        TextureRegion[][] tempFrames = TextureRegion.split(
                this.getTexture(), this.getTexture().getWidth() / FRAME_COLS,
                this.getTexture().getHeight() / FRAME_ROWS
        );

        TextureRegion[] idleFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] movingFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] collisionFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] damageFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] deathFrames = new TextureRegion[FRAME_COLS];

        for (int i = 0; i < FRAME_COLS; i++) {
            idleFrames[i] = tempFrames[0][i];
            movingFrames[i] = tempFrames[1][i];
            collisionFrames[i] = tempFrames[2][i];
            damageFrames[i] = tempFrames[3][i];
            deathFrames[i] = tempFrames[4][i];
        }

        idleAnimation = new Animation<>(FRAME_DURATION, idleFrames);
        movingAnimation = new Animation<>(FRAME_DURATION, movingFrames);
        collisionAnimation = new Animation<>(FRAME_DURATION, collisionFrames);
        damageAnimation = new Animation<>(FRAME_DURATION, damageFrames);
        deathAnimation = new Animation<>(FRAME_DURATION, deathFrames);

        this.setHeight(120);
        this.setWidth(120);
        currentAnimation = idleAnimation;
        damageCount = 0;
        isDead = false;
        deathAnimationStarted = false;
    }

    public void update(float deltaTime, boolean pressA, boolean pressD, boolean hasCollision, boolean hitByBomb) {
        if (isDead) {
            if (deathAnimationStarted) {
                elapsedTime += deltaTime;
                TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, false);
                sprite.setRegion(currentFrame);
                if (currentAnimation.isAnimationFinished(elapsedTime)) {
                    return;
                }
            }
            return;
        }

        elapsedTime += deltaTime;

        if (hitByBomb && !isDamaged && !isColliding) {
            damageCount++;
            if (damageCount >= 3) {
                isDead = true;
                currentAnimation = deathAnimation;
                elapsedTime = 0;
                deathAnimationStarted = true;
                return;
            } else {
                collisionTime = COLLISION_DURATION;
                isDamaged = true;
                currentAnimation = damageAnimation;
                elapsedTime = 0;
            }
        }

        if (hasCollision && !isColliding && !isDamaged) {
            collisionTime = COLLISION_DURATION;
            isColliding = true;
            currentAnimation = collisionAnimation;
            elapsedTime = 0;
        }

        if (collisionTime > 0) {
            collisionTime -= deltaTime;
            if (collisionTime <= 0) {
                isColliding = false;
                isDamaged = false;
            }
        }

        isMoving = pressA || pressD;

        if (!isColliding && !isDamaged) {
            if (isMoving) {
                currentAnimation = movingAnimation;
            } else {
                currentAnimation = idleAnimation;
            }
        }

        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);

        if (pressA) {
            if (!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
            this.setX(this.getX() - this.getSpeed() * deltaTime);
        } else if (pressD) {
            if (currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
            this.setX(this.getX() + this.getSpeed() * deltaTime);
        }

        sprite.setPosition(getX(), getY());
        sprite.setRegion(currentFrame);

        if (getX() < 0) setX(0);
        if (getX() > Gdx.graphics.getWidth() - getWidth()) setX(Gdx.graphics.getWidth() - getWidth());
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public boolean isDead() {
        return isDead;
    }
}
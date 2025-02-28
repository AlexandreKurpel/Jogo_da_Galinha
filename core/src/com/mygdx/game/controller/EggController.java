package com.mygdx.game.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Assets;

public class EggController extends GenericController {

    private Sprite sprite;
    private Animation<TextureRegion> collisionAnimation;
    private Animation<TextureRegion> movingAnimation;
    private float collisionTime;
    private boolean isColliding;

    private static final float COLLISION_DURATION = 1.0f;
    private float elapsedTime;
    private Animation<TextureRegion> currentAnimation;
    private static final int FRAME_COLS = 12;
    private static final int FRAME_ROWS = 5;
    private static final float FRAME_DURATION = 0.2f;

    private Sound eggCatchSound;
    private Boolean bomb = false;

    public EggController(float x, float y, int dificuldade) {
        this.setX(x);
        this.setY(y);

        // geracao de bombas conformde dificuldade
        if (dificuldade == 0) { // facil
            bomb = MathUtils.random() < 0.1f;
        } else if (dificuldade == 1) { // medio
            bomb = MathUtils.random() < 0.3f;
        } else { // dificil
            bomb = MathUtils.random() < 0.5f;
        }


        if (bomb) {
            this.setTexture(Assets.manager.get(Assets.BOMBASPRITE));
            sprite = new Sprite(this.getTexture(), 0, 0, 10, 10);
            sprite.setPosition(x, y);
            sprite.setSize(Gdx.graphics.getHeight() / 10f, Gdx.graphics.getHeight() / 10f);

            TextureRegion[][] tempFrames = TextureRegion.split(
                    this.getTexture(), this.getTexture().getWidth() / 6,
                    this.getTexture().getHeight() / 6
            );

            TextureRegion[] movingFrames = new TextureRegion[6];
            TextureRegion[] collisionFrames = new TextureRegion[6];

            for (int i = 0; i < 6; i++) {
                movingFrames[i] = tempFrames[0][i];  // Linha 1, 6 frames para o movimento
                collisionFrames[i] = tempFrames[1][i];  // Linha 4, 12 frames para a colisão
            }
            movingAnimation = new Animation<>(FRAME_DURATION, movingFrames);
            collisionAnimation = new Animation<>(FRAME_DURATION, collisionFrames);
            currentAnimation = movingAnimation;
            this.setEggCatchSound(Assets.manager.get(Assets.CATCH_BOMBA_SOUND));
        } else {
            this.setTexture(Assets.manager.get(Assets.SPRITEEGG));
            sprite = new Sprite(this.getTexture(), 0, 0, 10, 10);
            sprite.setPosition(x, y);
            sprite.setSize(Gdx.graphics.getHeight() / 15f, Gdx.graphics.getHeight() / 15f);

            TextureRegion[][] tempFrames = TextureRegion.split(
                    this.getTexture(), this.getTexture().getWidth() / FRAME_COLS,
                    this.getTexture().getHeight() / FRAME_ROWS
            );

            TextureRegion[] movingFrames = new TextureRegion[4];
            TextureRegion[] collisionFrames = new TextureRegion[FRAME_COLS];

            for (int i = 0; i < 4; i++) {
                movingFrames[i] = tempFrames[1][i];  // Linha 1, 6 frames para o movimento
            }

            for (int i = 0; i < 12; i++) {
                collisionFrames[i] = tempFrames[4][i];  // Linha 4, 12 frames para a colisão
            }
            movingAnimation = new Animation<>(FRAME_DURATION, movingFrames);
            collisionAnimation = new Animation<>(FRAME_DURATION, collisionFrames);
            currentAnimation = movingAnimation;
            this.setEggCatchSound(Assets.manager.get(Assets.CATCH_EGG_SOUND));
        }




    }

    public void update(float deltaTime, boolean hasCollision) {
        elapsedTime += deltaTime;  // Incrementa o tempo decorrido

        if (hasCollision) {
            // Se há colisão, inicia o tempo de colisão e altera a animação
            collisionTime = COLLISION_DURATION;
            isColliding = true;
            currentAnimation = collisionAnimation;
            elapsedTime = 0;  // Resetando o tempo da animação para colisão
        }

        if (collisionTime > 0) {
            collisionTime -= deltaTime;  // Decrementa o tempo de colisão
            if (collisionTime <= 0) {
                isColliding = false;
            }
        }

        if (!isColliding) {
            currentAnimation = movingAnimation;  // Se não houver colisão, usa a animação de movimento
        }

        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);  // Obtém o quadro atual da animação

        // Movimenta o ovo para baixo
        setY(getY() - 200 * deltaTime);  // O ovo cai a uma velocidade de 200 pixels por segundo

        sprite.setPosition(getX(), getY());
        sprite.setRegion(currentFrame);  // Atualiza a região do sprite com o quadro da animação

    }

    public boolean isOutOfBounds() {
        return getY() + getHeight() < 0;  // Verifica se o ovo saiu da tela
    }

    public Boolean getBomb() {
        return bomb;
    }

    public void setBomb(Boolean bomb) {
        this.bomb = bomb;
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);  // Desenha o sprite na tela
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public Sound getEggCatchSound() {
        return eggCatchSound;
    }

    public void setEggCatchSound(Sound eggCatchSound) {
        this.eggCatchSound = eggCatchSound;
    }
}

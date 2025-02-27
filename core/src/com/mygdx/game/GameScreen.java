package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.controller.ChickenController;
import com.mygdx.game.controller.CobraController;
import com.mygdx.game.controller.EggController;

class GameScreen implements Screen {
    private final MyGdxGame game;
    private CobraController cobra;
    private Array<ChickenController> chickens;
    private long lastEggTime;
    private BitmapFont font;
    private int score;
    private static final float GAME_DURATION = 60f;
    private float timeRemaining;
    private boolean gameEnded;

    public GameScreen(MyGdxGame game) {
        this.game = game;
        initialize();
    }

    private void initialize() {
        Assets.load();
        Assets.manager.finishLoading();
        Assets.showMusic();

        cobra = new CobraController();
        chickens = new Array<>();

        for (int i = 0; i < 4; i++) {
            ChickenController chicken = new ChickenController();
            float aux = (i % 4f) * (Gdx.graphics.getWidth() / 4f) + ((Gdx.graphics.getWidth() / 4f) / 2f);
            chicken.setX(aux);
            chickens.add(chicken);
        }

        font = new BitmapFont();
        score = 0;
        timeRemaining = GAME_DURATION;
        gameEnded = false;
    }

    private void spawnEgg() {
        ChickenController chicken = chickens.random();
        chicken.shoot();
        lastEggTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        Gdx.graphics.setContinuousRendering(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!gameEnded) {
            timeRemaining -= delta;
            if (timeRemaining <= 0) {
                gameEnded = true;
                timeRemaining = 0;
            }
        }

        game.getBatch().begin();
        game.getBatch().draw((Texture) Assets.manager.get(Assets.BACKGROUND_TEXTURE), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (!gameEnded) {
            for (ChickenController chicken : chickens) {
                chicken.render(game.getBatch());
            }

            cobra.render(game.getBatch());
            drawBackground("Score: " + score, 10, Gdx.graphics.getHeight() - 10);
            drawBackground("Time: " + (int) timeRemaining, 10, Gdx.graphics.getHeight() - 40);
        } else {
            drawBackground("Game Over!", Gdx.graphics.getWidth() / 2f - 70, Gdx.graphics.getHeight() / 2f + 20);
            drawBackground("Final Score: " + score, Gdx.graphics.getWidth() / 2f - 90, Gdx.graphics.getHeight() / 2f - 10);
            drawBackground("Press 'R' to Restart", Gdx.graphics.getWidth() / 2f - 110, Gdx.graphics.getHeight() / 2f - 40);

            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                game.setScreen(new MenuScreen(game));
            }
        }

        game.getBatch().end();

        if (!gameEnded) {
            boolean hasCollision = false;
            boolean hitByBomb = false;

            cobra.update(delta, game.inputProcessor.keyAPress, game.inputProcessor.keyDPress, false, false);

            for (ChickenController chicken : chickens) {
                int cont = 0;
                for (EggController egg : chicken.getEggs()) {
                    egg.update(delta);

                    if (verificaColisao(egg, cobra)) {
                        chicken.getEggs().removeIndex(cont);
                        egg.getEggCatchSound().play();

                        if (egg.getBomb()) {
                            hitByBomb = true;
                        } else {
                            score++;
                            hasCollision = true;
                        }
                    }

                    if (egg.isOutOfBounds()) {
                        chicken.getEggs().removeIndex(cont);
                    }
                    cont++;
                }
            }

            cobra.update(delta, game.inputProcessor.keyAPress, game.inputProcessor.keyDPress, hasCollision, hitByBomb);

            if (cobra.isDead()) {
                gameEnded = true;
            }

            if (TimeUtils.nanoTime() - lastEggTime > 1_000_000_000) {
                spawnEgg();
            }
        }
    }

    private void drawBackground(String text, float x, float y) {
        game.getBatch().setColor(Color.BLACK);
        game.getBatch().draw((Texture) Assets.manager.get(Assets.BACKGROUND_TEXTURE), x - 5, y - 20, 200, 25);
        game.getBatch().setColor(Color.WHITE);
        font.setColor(Color.WHITE);
        font.draw(game.getBatch(), text, x, y);
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        font.dispose();
        for (ChickenController chicken : chickens) {
            chicken.dispose();
        }
    }

    private boolean verificaColisao(Colisao obj1, Colisao obj2) {
        return obj1.getX() < obj2.getX() + obj2.getWidth() &&
                obj1.getX() + obj1.getWidth() > obj2.getX() &&
                obj1.getY() < obj2.getY() + obj2.getHeight() &&
                obj1.getY() + obj1.getHeight() > obj2.getY();
    }
}
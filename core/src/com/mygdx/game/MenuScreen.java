package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;

public class MenuScreen implements Screen {

    private final MyGdxGame game;
    private BitmapFont font;
    private int selectedOption = 0; // 0 = Fácil, 1 = Médio, 2 = Difícil
    private final String[] difficulties = {"Fácil", "Médio", "Difícil"};
    private final float[] speedValues = {Gdx.graphics.getWidth() / 2f, Gdx.graphics.getWidth() / 3f, Gdx.graphics.getWidth() / 4f}; // Velocidade do personagem

    public MenuScreen(MyGdxGame game) {
        this.game = game;
        font = new BitmapFont();
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        SpriteBatch batch = game.getBatch();
        batch.begin();

        font.draw(batch, "Bem-vindo ao Jogo DO RAPOSO!", Gdx.graphics.getWidth() / 2f - 115, Gdx.graphics.getHeight() / 2f + 60);
        font.draw(batch, "Escolha a dificuldade(W - S):", Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f + 20);

        for (int i = 0; i < difficulties.length; i++) {
            if (i == selectedOption) {
                font.draw(batch, "> " + difficulties[i] + " <", Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f - (i * 30));
            } else {
                font.draw(batch, difficulties[i], Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f - (i * 30));
            }
        }

        font.draw(batch, "Pressione ENTER para começar", Gdx.graphics.getWidth() / 2f - 110, Gdx.graphics.getHeight() / 2f - 100);
        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectedOption = (selectedOption - 1 + difficulties.length) % difficulties.length;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedOption = (selectedOption + 1) % difficulties.length;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            System.out.println(selectedOption);
            game.setScreen(new GameScreen(game, selectedOption)); // Passa a velocidade escolhida
        }
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
    }
}

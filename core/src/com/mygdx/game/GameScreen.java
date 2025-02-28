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
    private float deathTimer;
    private static final float DEATH_ANIMATION_DURATION = 1.6f;
    private int dificuldade = 0;
    private int vidas;
    private float velocidade;

    public GameScreen(MyGdxGame game, int dificuldade) {
        setDificuldade(dificuldade);
        this.game = game;
        initialize();
    }

    //Função para pegar a dificuldade, quantidade de vidas e velocidade
    private void setDificuldade(int dificuldade) {
        switch (dificuldade) {
            case 0:
                vidas = 5; // facil
                velocidade = Gdx.graphics.getWidth() / 3f;
                break;
            case 1:
                vidas = 3; // medio
                velocidade = Gdx.graphics.getWidth() / 4f;
                break;
            case 2:
                vidas = 1; // dificil
                velocidade = Gdx.graphics.getWidth() / 5.5f;
                break;
            default:
                vidas = 3; // medio
                velocidade = Gdx.graphics.getWidth() / 4f;
                break;
        }
    }

    private void initialize() {
        Assets.load();
        Assets.manager.finishLoading();
        Assets.showMusic();

        cobra = new CobraController(velocidade, vidas);
        chickens = new Array<>();

        for (int i = 0; i < 4; i++) {
            ChickenController chicken = new ChickenController(dificuldade);
            float aux = (i % 4f) * (Gdx.graphics.getWidth() / 4f) + ((Gdx.graphics.getWidth() / 4f) / 2f);
            chicken.setX(aux);
            chickens.add(chicken);
        }

        font = new BitmapFont();
        font.setColor(Color.WHITE); // Cor do texto
        score = 0;
        timeRemaining = GAME_DURATION;
        gameEnded = false;
        deathTimer = 0f;
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

        if (!gameEnded || deathTimer > 0) {
            for (ChickenController chicken : chickens) {
                chicken.render(game.getBatch());
            }

            cobra.render(game.getBatch());

            // Interface do jogo
            drawInterface();

        } else {
            drawGameOverScreen();
        }

        game.getBatch().end();

        if (!gameEnded) {
            boolean hasCollision = false;
            boolean hitByBomb = false;

            cobra.update(delta, game.inputProcessor.keyAPress, game.inputProcessor.keyDPress, false, false);

            for (ChickenController chicken : chickens) {
                int cont = 0;
                for (EggController egg : chicken.getEggs()) {
                    egg.update(delta,false);

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
                if (deathTimer == 0f) {
                    deathTimer = DEATH_ANIMATION_DURATION;
                }
                deathTimer -= delta;
                if (deathTimer <= 0) {
                    gameEnded = true;
                }
            }

            if (TimeUtils.nanoTime() - lastEggTime > 1_000_000_000) {
                spawnEgg();
            }
        }
    }

    private void drawInterface() {
        int yOffset = Gdx.graphics.getHeight() - 20;
        int xOffset = 20;

        // Pontuação
        drawBackground("Score: " + score, xOffset, yOffset);
        yOffset -= 30;

        // Tempo
        drawBackground("Time: " + (int) timeRemaining, xOffset, yOffset);
        yOffset -= 30;

        // Vida
        drawBackground("Vida: " + (vidas - cobra.damageCount), xOffset, yOffset);
    }

    private void drawGameOverScreen() {
        int centerX = Gdx.graphics.getWidth() / 2;
        int centerY = Gdx.graphics.getHeight() / 2;

        game.getBatch().setColor(Color.BLACK);
        game.getBatch().draw((Texture) Assets.manager.get(Assets.BACKGROUND_TEXTURE), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().setColor(Color.WHITE);

        font.setColor(Color.WHITE);
        font.getData().setScale(2); // Aumenta a escala da fonte

        String gameOverMessage = "Game Over!";
        String finalScoreMessage = "Final Score: " + score;
        String restartMessage = "Press 'R' to Restart";

        // Mensagens personalizadas
        if (score >= 20) {
            gameOverMessage = "Congratulations!";
            finalScoreMessage = "You Scored: " + score;
        } else if (score <= 5) {
            gameOverMessage = "Try Again!";
        }

        float gameOverWidth = font.getData().getGlyph(' ').width * gameOverMessage.length();
        float finalScoreWidth = font.getData().getGlyph(' ').width * finalScoreMessage.length();
        float restartWidth = font.getData().getGlyph(' ').width * restartMessage.length();

        font.draw(game.getBatch(), gameOverMessage, centerX - gameOverWidth / 2, centerY + 50);
        font.draw(game.getBatch(), finalScoreMessage, centerX - finalScoreWidth / 2, centerY);
        font.draw(game.getBatch(), restartMessage, centerX - restartWidth / 2, centerY - 50);

//        font.getData().setScale(1); // Restaura a escala da fonte

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    private void drawBackground(String text, float x, float y) {
        game.getBatch().setColor(Color.BLACK);
        game.getBatch().draw((Texture) Assets.manager.get(Assets.BACKGROUND_TEXTURE), x - 5, y - 20, 150, 25);
        game.getBatch().setColor(Color.WHITE);
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
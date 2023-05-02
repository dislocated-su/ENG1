package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioSystem {
    private Music bgm = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
    private String sizzleSfx = "sizzle.ogg";
    private String tapSfx = "tap.ogg";
    private String chopSfx = "chop.ogg";
    private String dingSfx = "ding.ogg";
    private String thanksSfx = "thanks.ogg";
    private String sighSfx = "sigh.ogg";

    private boolean muted = false;

    public void toggleMute() {
        if (muted) {
            muted = false;
            bgm.play();
        } else {
            muted = true;
            bgm.stop();
        }
    }

    public void playBgm() {
        bgm.setLooping(true);
        bgm.setVolume(1);
        bgm.play();
    }

    public void playSizzle() {
        if (muted) {
            return;
        }
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(sizzleSfx));
        sfx.play();
    }

    public void playTap() {
        if (muted) {
            return;
        }
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(tapSfx));
        sfx.play();
    }

    public void playChop() {
        if (muted) {
            return;
        }
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(chopSfx));
        sfx.play();
    }

    public void playDing() {
        if (muted) {
            return;
        }
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(dingSfx));
        sfx.play();
    }

    public void playThanks() {
        if (muted) {
            return;
        }
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(thanksSfx));
        sfx.play();
    }

    public void playSigh() {
        if (muted) {
            return;
        }
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(sighSfx));
        sfx.play();
    }
}

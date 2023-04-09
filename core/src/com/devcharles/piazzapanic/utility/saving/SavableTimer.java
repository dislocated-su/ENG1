package com.devcharles.piazzapanic.utility.saving;

import com.devcharles.piazzapanic.utility.GdxTimer;

public class SavableTimer {
  public int delay;
  public int elapsed;
  public boolean running;
  public boolean looping;

  public static SavableTimer from(GdxTimer gdxTimer) {
    SavableTimer timer = new SavableTimer();
    timer.delay = gdxTimer.getDelay();
    timer.elapsed = gdxTimer.getElapsed();
    timer.running = gdxTimer.isRunning();
    timer.looping = gdxTimer.isLooping();
    return timer;
  }

  public GdxTimer toGdxTimer() {
    GdxTimer newTimer = new GdxTimer(delay, running, looping);
    newTimer.setElapsed(elapsed);
    return newTimer;
  }
}

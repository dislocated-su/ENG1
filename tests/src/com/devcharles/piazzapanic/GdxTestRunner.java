package com.devcharles.piazzapanic;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class GdxTestRunner extends BlockJUnit4ClassRunner implements ApplicationListener {

  private final Map<FrameworkMethod, RunNotifier> invokeInRender = new HashMap<>();

  public GdxTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
    HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();

    new HeadlessApplication(this, conf);
    Gdx.gl20 = mock(GL20.class);
    when(Gdx.gl20.glCheckFramebufferStatus(anyInt())).thenReturn(GL20.GL_FRAMEBUFFER_COMPLETE);

    Gdx.gl = Gdx.gl20;
  }

  @Override
  public void create() {

  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void render() {
    synchronized (invokeInRender) {
      for (Entry<FrameworkMethod, RunNotifier> each : invokeInRender.entrySet()) {
        super.runChild(each.getKey(), each.getValue());
      }
      invokeInRender.clear();
    }
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }

  @Override
  protected void runChild(FrameworkMethod method, RunNotifier notifier) {
    synchronized (invokeInRender) {
      // add for invoking in render phase, where gl context is available
      invokeInRender.put(method, notifier);
    }
    // wait until that test was invoked
    waitUntilInvokedInRenderMethod();
  }

  private void waitUntilInvokedInRenderMethod() {
    try {
      while (true) {
        Thread.sleep(10);
        synchronized (invokeInRender) {
          if (invokeInRender.isEmpty()) {
            break;
          }
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

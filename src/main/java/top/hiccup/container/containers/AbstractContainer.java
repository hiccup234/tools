package top.hiccup.container.containers;

import top.hiccup.container.Container;

/**
 * 抽象容器类
 *
 * @author wenhy
 * @date 2019/9/5
 */
public abstract class AbstractContainer implements Container {

    public void beforeStart() {

    }

    public abstract void doStart();

    public void afterStart() {

    }

    @Override
    public void start() {
        this.beforeStart();
        this.doStart();
        this.afterStart();
    }

    @Override
    public void stop() {

    }
}

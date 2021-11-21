package ca.cgjennings.apps.arkham.plugins;

/**
 * DevToolProxy for the memory use window.
 *
 * @author Christopher G. Jennings (cjennings@acm.org)
 */
final class MemoryUseProxy extends DevToolProxy {

    public MemoryUseProxy() {
        super("Memory Use", 0f);
    }

    @Override
    public void unload() {
        if (tw != null) {
            ((MemoryGraph) tw.getBody().getComponent(0)).dispose();
        }
        super.unload();
    }
}

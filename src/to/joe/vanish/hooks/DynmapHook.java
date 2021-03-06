package to.joe.vanish.hooks;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapPlugin;

import to.joe.vanish.VanishPlugin;

public class DynmapHook {
    private final VanishPlugin plugin;

    private DynmapPlugin dynmap;
    private boolean enabled;

    public DynmapHook(VanishPlugin plugin) {
        this.plugin = plugin;
        this.enabled = false;
    }

    public void onPluginDisable() {
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            if ((player != null) && this.plugin.getManager().isVanished(player)) {
                this.unvanish(player);
            }
        }
    }

    public void onPluginEnable(boolean enableEssentials) {
        this.enabled = enableEssentials;
        if (enableEssentials) {
            this.grabDynmap();
        } else {
            this.dynmap = null;
        }
    }

    public void unvanish(Player player) {
        if(this.enabled && this.dynmap!=null){
            this.dynmap.playerList.show(player.getName());
        }
    }

    public void vanish(Player player) {
        if(this.enabled && this.dynmap!=null){
            this.dynmap.playerList.hide(player.getName());
        }
    }

    private void grabDynmap() {
        final Plugin grab = this.plugin.getServer().getPluginManager().getPlugin("dynmap");
        if (grab != null) {
            this.dynmap = ((DynmapPlugin) grab);
            this.plugin.log("Now hooking into Dynmap");
        }
        else{
            this.plugin.log("You wanted Dynmap support. I could not find Dynmap.");
            this.dynmap = null;
            this.enabled = false;
        }
    }
}

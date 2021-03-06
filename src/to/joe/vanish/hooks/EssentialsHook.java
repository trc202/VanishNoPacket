package to.joe.vanish.hooks;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.IEssentials;

import to.joe.vanish.VanishPlugin;

public class EssentialsHook {
    private final VanishPlugin plugin;

    private IEssentials essentials;
    private boolean enabled;

    public EssentialsHook(VanishPlugin plugin) {
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
            this.grabEssentials();
        } else {
            this.essentials = null;
        }
    }

    public void unvanish(Player player) {
        this.setHidden(player, false);
    }

    public void vanish(Player player) {
        this.setHidden(player, true);
    }

    private void grabEssentials() {
        final Plugin grab = this.plugin.getServer().getPluginManager().getPlugin("Essentials");
        if (grab != null) {
            this.essentials = ((IEssentials) grab);
            this.plugin.log("Now hooking into Essentials");
        }
        else{
            this.plugin.log("You wanted Essentials support. I could not find Essentials.");
            this.essentials = null;
            this.enabled = false;
        }
    }

    private void setHidden(Player player, boolean hide) {
        if (this.enabled && (this.essentials != null)) {
            this.essentials.getUser(player).setHidden(hide);
        }
    }
}

package org.bukkit.event.entity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Thrown whenever a {@link Player} dies
 */
public class PlayerDeathEvent extends EntityDeathEvent {
    private int newExp = 0;
    private String deathMessage = "";
    private net.kyori.adventure.text.Component adventure$deathMessage; // Paper
    private int newLevel = 0;
    private int newTotalExp = 0;
    private boolean keepLevel = false;
    private boolean keepInventory = false;
    // Paper start
    private boolean doExpDrop;

    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, @Nullable final net.kyori.adventure.text.Component adventure$deathMessage) {
        this(player, drops, droppedExp, 0, adventure$deathMessage, null);
    }

    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, @Nullable final net.kyori.adventure.text.Component adventure$deathMessage, @Nullable String deathMessage) {
        this(player, drops, droppedExp, newExp, 0, 0, adventure$deathMessage, deathMessage);
    }

    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final net.kyori.adventure.text.Component adventure$deathMessage, @Nullable String deathMessage) {
        this(player, drops, droppedExp, newExp, newTotalExp, newLevel, adventure$deathMessage, deathMessage, true);
    }

    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final net.kyori.adventure.text.Component adventure$deathMessage, @Nullable String deathMessage, boolean doExpDrop) {
        super(player, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = deathMessage;
        this.adventure$deathMessage = adventure$deathMessage;
        this.doExpDrop = doExpDrop;
    }
    // Paper end

    @Deprecated // Paper
    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, @Nullable final String deathMessage) {
        this(player, drops, droppedExp, 0, deathMessage);
    }

    @Deprecated // Paper
    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, @Nullable final String deathMessage) {
        this(player, drops, droppedExp, newExp, 0, 0, deathMessage);
    }

    @Deprecated // Paper
    public PlayerDeathEvent(@NotNull final Player player, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final String deathMessage) {
        super(player, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = deathMessage;
    }

    @NotNull
    @Override
    public Player getEntity() {
        return (Player) entity;
    }

    @Deprecated // Paper
    // Paper start
    private List<ItemStack> itemsToKeep = new java.util.ArrayList<>();

    /**
     * A mutable collection to add items that the player should retain in their inventory on death (Similar to KeepInventory game rule)
     *
     * You <b>MUST</b> remove the item from the .getDrops() collection too or it will duplicate!
     * <pre>{@code
     *    {@literal @EventHandler(ignoreCancelled = true)}
     *     public void onPlayerDeath(PlayerDeathEvent event) {
     *         for (Iterator<ItemStack> iterator = event.getDrops().iterator(); iterator.hasNext(); ) {
     *             ItemStack drop = iterator.next();
     *             List<String> lore = drop.getLore();
     *             if (lore != null && !lore.isEmpty()) {
     *                 if (lore.get(0).contains("(SOULBOUND)")) {
     *                     iterator.remove();
     *                     event.getItemsToKeep().add(drop);
     *                 }
     *             }
     *         }
     *     }
     * }</pre>
     *
     * Adding an item to this list that the player did not previously have will give them the item on death.
     * An example case could be a "Note" that "You died at X/Y/Z coordinates"
     *
     * @return The list to hold items to keep
     */
    @NotNull
    public List<ItemStack> getItemsToKeep() {
        return itemsToKeep;
    }

    /**
     * @return should experience be dropped from this death
     */
    public boolean shouldDropExperience() {
        return doExpDrop;
    }

    /**
     * @param doExpDrop sets if experience should be dropped from this death
     */
    public void setShouldDropExperience(boolean doExpDrop) {
        this.doExpDrop = doExpDrop;
    }
    // Paper end

    // Paper start
    /**
     * Clarity method for getting the player. Not really needed except
     * for reasons of clarity.
     *
     * @return Player who is involved in this event
     */
    @NotNull
    public Player getPlayer() {
        return getEntity();
    }

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Message to appear to other players on the server.
     */
    @Nullable
    public void deathMessage(net.kyori.adventure.text.Component deathMessage) {
        this.deathMessage = null;
        this.adventure$deathMessage = deathMessage;
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     */
    @Nullable
    public net.kyori.adventure.text.Component deathMessage() {
        return this.adventure$deathMessage;
    }
    // Paper end

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Message to appear to other players on the server.
     */
    @Deprecated // Paper
    public void setDeathMessage(@Nullable String deathMessage) {
        this.deathMessage = deathMessage;
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     * @deprecated in favour of {@link #deathMessage()}
     */
    @Nullable
    @Deprecated // Paper
    public String getDeathMessage() {
        return this.deathMessage != null ? this.deathMessage : (this.adventure$deathMessage != null ? getDeathMessageString(this.adventure$deathMessage) : null); // Paper
    }
    // Paper start //TODO: add translation API to drop String deathMessage in favor of just Adventure
    private static String getDeathMessageString(net.kyori.adventure.text.Component component) {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(component);
    }
    // Paper end

    /**
     * Gets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #getDroppedExp()} for that.
     *
     * @return New EXP of the respawned player
     */
    public int getNewExp() {
        return newExp;
    }

    /**
     * Sets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #setDroppedExp(int)} for that.
     *
     * @param exp New EXP of the respawned player
     */
    public void setNewExp(int exp) {
        newExp = exp;
    }

    /**
     * Gets the Level the Player should have at respawn.
     *
     * @return New Level of the respawned player
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Sets the Level the Player should have at respawn.
     *
     * @param level New Level of the respawned player
     */
    public void setNewLevel(int level) {
        newLevel = level;
    }

    /**
     * Gets the Total EXP the Player should have at respawn.
     *
     * @return New Total EXP of the respawned player
     */
    public int getNewTotalExp() {
        return newTotalExp;
    }

    /**
     * Sets the Total EXP the Player should have at respawn.
     *
     * @param totalExp New Total EXP of the respawned player
     */
    public void setNewTotalExp(int totalExp) {
        newTotalExp = totalExp;
    }

    /**
     * Gets if the Player should keep all EXP at respawn.
     * <p>
     * This flag overrides other EXP settings
     *
     * @return True if Player should keep all pre-death exp
     */
    public boolean getKeepLevel() {
        return keepLevel;
    }

    /**
     * Sets if the Player should keep all EXP at respawn.
     * <p>
     * This overrides all other EXP settings
     * <p>
     * <b>This doesn't prevent the EXP from dropping.
     * {@link #setDroppedExp(int)} should be used stop the
     * EXP from dropping.</b>
     *
     * @param keepLevel True to keep all current value levels
     */
    public void setKeepLevel(boolean keepLevel) {
        this.keepLevel = keepLevel;
    }

    /**
     * Sets if the Player keeps inventory on death.
     * <p>
     * <b>This doesn't prevent the items from dropping.
     * {@code getDrops().clear()} should be used stop the
     * items from dropping.</b>
     *
     * @param keepInventory True to keep the inventory
     */
    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    /**
     * Gets if the Player keeps inventory on death.
     *
     * @return True if the player keeps inventory on death
     */
    public boolean getKeepInventory() {
        return keepInventory;
    }
}

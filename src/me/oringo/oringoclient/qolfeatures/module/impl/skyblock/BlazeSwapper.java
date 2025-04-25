// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.qolfeatures.module.impl.skyblock;

import net.minecraft.item.ItemStack;
import me.oringo.oringoclient.utils.PlayerUtils;
import me.oringo.oringoclient.utils.PacketUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import me.oringo.oringoclient.mixins.PlayerControllerAccessor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemSword;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySkeleton;
import me.oringo.oringoclient.events.PreAttackEvent;
import me.oringo.oringoclient.qolfeatures.module.settings.Setting;
import me.oringo.oringoclient.utils.MilliTimer;
import me.oringo.oringoclient.qolfeatures.module.settings.impl.BooleanSetting;
import me.oringo.oringoclient.qolfeatures.module.Module;

public class BlazeSwapper extends Module
{
    public static final BooleanSetting ghost;
    private static final String[] ashNames;
    private static final String[] spiritNames;
    private final MilliTimer delay;
    
    public BlazeSwapper() {
        super("Blaze Swapper", Category.SKYBLOCK);
        this.delay = new MilliTimer();
        this.addSettings(BlazeSwapper.ghost);
    }
    
    @SubscribeEvent
    public void onAttack(final PreAttackEvent event) {
        if (this.isToggled() && ((event.entity instanceof EntitySkeleton && ((EntitySkeleton)event.entity).func_82202_m() == 1) || event.entity instanceof EntityBlaze || event.entity instanceof EntityPigZombie)) {
            final List<EntityArmorStand> armorStands = (List<EntityArmorStand>)BlazeSwapper.mc.field_71441_e.func_175647_a((Class)EntityArmorStand.class, event.entity.func_174813_aQ().func_72314_b(0.1, 2.0, 0.1), entity -> {
                final String text = entity.func_145748_c_().func_150260_c().toLowerCase();
                return text.contains("spirit") || text.contains("ashen") || text.contains("auric") || text.contains("crystal");
            });
            if (!armorStands.isEmpty()) {
                final EntityArmorStand armorStand = armorStands.get(0);
                final Type type = this.getType(armorStand.func_145748_c_().func_150260_c());
                if (type != Type.NONE) {
                    final int slot = getSlot(type);
                    if (slot != -1) {
                        swap(slot);
                        if (this.delay.hasTimePassed(500L) && BlazeSwapper.mc.field_71439_g.field_71071_by.func_70301_a(slot).func_77973_b() instanceof ItemSword && !((ItemSword)BlazeSwapper.mc.field_71439_g.field_71071_by.func_70301_a(slot).func_77973_b()).func_150932_j().equals(type.material)) {
                            BlazeSwapper.mc.func_147114_u().func_147298_b().func_179290_a((Packet)new C08PacketPlayerBlockPlacement(BlazeSwapper.mc.field_71439_g.field_71071_by.func_70301_a(slot)));
                            this.delay.reset();
                        }
                    }
                }
            }
        }
    }
    
    private Type getType(final String name) {
        for (final Type type : Type.values()) {
            if (name.toLowerCase().contains(type.toString().toLowerCase())) {
                return type;
            }
        }
        return Type.NONE;
    }
    
    private static void swap(final int slot) {
        if (BlazeSwapper.ghost.isEnabled()) {
            if (((PlayerControllerAccessor)BlazeSwapper.mc.field_71442_b).getCurrentPlayerItem() != slot) {
                PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(slot));
                ((PlayerControllerAccessor)BlazeSwapper.mc.field_71442_b).setCurrentPlayerItem(-1);
            }
        }
        else {
            PlayerUtils.swapToSlot(slot);
        }
    }
    
    private static int getSlot(final Type type) {
        if (type == Type.NONE) {
            return -1;
        }
        boolean flag;
        final String[] array;
        final int length;
        int i = 0;
        String name;
        return PlayerUtils.getHotbar(stack -> {
            flag = false;
            array = ((type == Type.ASHEN || type == Type.AURIC) ? BlazeSwapper.ashNames : BlazeSwapper.spiritNames);
            length = array.length;
            while (i < length) {
                name = array[i];
                if (stack.func_82833_r().contains(name)) {
                    flag = true;
                    break;
                }
                else {
                    ++i;
                }
            }
            return stack.func_77973_b() instanceof ItemSword && flag;
        });
    }
    
    static {
        ghost = new BooleanSetting("Ghost", false);
        ashNames = new String[] { "Firedust", "Kindlebane", "Pyrochaos" };
        spiritNames = new String[] { "Mawdredge", "Twilight", "Deathripper" };
    }
    
    public enum Type
    {
        ASHEN("STONE"), 
        AURIC("GOLD"), 
        CRYSTAL("EMERALD"), 
        SPIRIT("IRON"), 
        NONE("NONE");
        
        public String material;
        
        private Type(final String material) {
            this.material = material;
        }
    }
}

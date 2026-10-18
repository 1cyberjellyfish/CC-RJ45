//? if neoforge {
/*package net.cyber_jellyfish.cc_rj45.client;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.ModDataComponents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

//? if <=1.21.1 {
/^@EventBusSubscriber(modid = CCRJ45.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CCRJ45.NET_CABLE_CONNECTOR_BE.get(), NetCableConnectorRenderer::new);
        event.registerBlockEntityRenderer(CCRJ45.NET_CABLE_HUB_BE.get(), NetCableConnectorRenderer::new);
        event.registerBlockEntityRenderer(CCRJ45.NET_CABLE_CONNECTOR_HUGE_BE.get(), NetCableConnectorRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> {
                    if (tintIndex == 1) {
                        var color = stack.get(ModDataComponents.DISK_COLOR.get());
                        return color != null ? 0xFF000000 | color.color() : -1;
                    }
                    return -1;
                },
                CCRJ45.PORTABLE_DRIVE.get()
        );
    }
}
^///?}
*///?}

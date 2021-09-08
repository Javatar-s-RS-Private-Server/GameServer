package com.arandarkt.game.api.koin

import com.arandarkt.game.api.entity.components.items.DefinitionComponent
import com.arandarkt.game.api.entity.components.items.EquipmentComponent
import com.arandarkt.game.api.entity.components.player.apperance.AppearanceComponent
import com.arandarkt.game.api.entity.item.GameItem
import com.arandarkt.game.api.entity.item.GameItemBuilder
import com.arandarkt.game.api.world.location.components.PositionComponent
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
) = GlobalContext.get().inject<T>(qualifier, mode, parameters)

inline fun <reified T : Any> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = GlobalContext.get().get(qualifier, parameters)

fun emptyItem() = newItem(-1, 0).build()

fun newItem(itemId: Int, amount: Int = 1): GameItemBuilder {
    return GlobalContext.get().get<GameItemBuilder>().item(itemId, amount)
}

fun newDefinedItem(itemId: Int, amount: Int = 1) = newItem(itemId, amount).with(DefinitionComponent())

fun newStackedItem(itemId: Int, amount: Int = 1) = newItem(itemId, amount).with(DefinitionComponent(true))

fun newEquipment(itemId: Int, amount: Int = 1, slot: Int = AppearanceComponent.SLOT_WEAPON) =
    newItem(itemId, amount)
        .withIndex(slot)
        .with(DefinitionComponent())
        .with(EquipmentComponent())

fun newStackedEquipment(itemId: Int, amount: Int = 1, slot: Int = AppearanceComponent.SLOT_ARROWS) =
    newItem(itemId, amount)
        .withIndex(slot)
        .with(DefinitionComponent(true))
        .with(EquipmentComponent())

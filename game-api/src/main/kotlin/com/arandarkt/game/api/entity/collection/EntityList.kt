package com.arandarkt.game.api.entity.collection

import com.arandarkt.game.api.entity.Entity

interface EntityList<T : Entity> : Collection<T?>, Iterable<T?> {



}
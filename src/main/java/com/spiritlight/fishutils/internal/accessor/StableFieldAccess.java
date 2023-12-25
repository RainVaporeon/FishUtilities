package com.spiritlight.fishutils.internal.accessor;

import com.spiritlight.fishutils.misc.StableField;

public interface StableFieldAccess {

    <T> void updateField(StableField<T> field, T value);

}

package world.wltp.wltpmorphplugin;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum MorphType {
    ZOMBIE(EntityType.ZOMBIE),
    SKELETON(EntityType.SKELETON),
    CREEPER(EntityType.CREEPER),

    DIAMOND_BLOCK(null);  // 블럭은 특별 처리

    public final EntityType entityType;

    MorphType(EntityType type) {
        this.entityType = type;
    }

    public static MorphType fromString(String name) {
        try {
            return MorphType.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isBlock() {
        return this == DIAMOND_BLOCK;
    }

    public Material getBlockMaterial() {
        return isBlock() ? Material.DIAMOND_BLOCK : null;
    }
}

package wraith.waystones.mixin;

import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.HeightLimitView;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import wraith.waystones.util.WaystonesWorldgen;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(StructurePoolBasedGenerator.StructurePoolGenerator.class)
public class StructurePoolBasedGeneratorMixin {

    @Shadow
    @Final
    private List<? super PoolStructurePiece> children;

    @Inject(method = "generatePiece(Lnet/minecraft/structure/PoolStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/HeightLimitView;)V",
        at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z", ordinal = 0, shift = At.Shift.AFTER, remap = false),
        locals = LocalCapture.CAPTURE_FAILSOFT)
    private void fabricwaystones_limitWaystonePieceSpawning(PoolStructurePiece piece,
                                                            MutableObject<VoxelShape> pieceShape,
                                                            int minY,
                                                            boolean modifyBoundingBox,
                                                            HeightLimitView world,
                                                            CallbackInfo ci,
                                                            StructurePoolElement structurePoolElement,
                                                            BlockPos blockPos,
                                                            BlockRotation blockRotation,
                                                            StructurePool.Projection projection,
                                                            boolean bl,
                                                            MutableObject<VoxelShape> mutableObject,
                                                            BlockBox blockBox,
                                                            int i,
                                                            Iterator<Structure.StructureBlockInfo> var14,
                                                            Structure.StructureBlockInfo structureBlockInfo,
                                                            Direction direction,
                                                            BlockPos blockPos2,
                                                            BlockPos blockPos3,
                                                            int j,
                                                            int k,
                                                            Identifier identifier,
                                                            Optional<StructurePool> optional,
                                                            Identifier identifier2,
                                                            Optional<StructurePool> optional2,
                                                            MutableObject<Object> mutableObject2,
                                                            boolean bl2,
                                                            List<StructurePoolElement> list)
    {
        if(optional.isPresent() && WaystonesWorldgen.VANILLA_VILLAGES.containsKey(optional.get().getId())) {
            if(children.stream().anyMatch(element -> {
                if(element instanceof PoolStructurePiece poolStructurePiece &&
                        poolStructurePiece.getPoolElement() instanceof SinglePoolElement singlePoolElement) {
                    return ((SinglePoolElementAccessor)singlePoolElement).getLocation().left().orElse(new Identifier("empty")).getNamespace().equals("waystones");
                }
                return false;
            })) {
                list.removeIf(element -> {
                    if(element instanceof SinglePoolElement singlePoolElement) {
                        return ((SinglePoolElementAccessor)singlePoolElement).getLocation().left().orElse(new Identifier("empty")).getNamespace().equals("waystones");
                    }
                    return false;
                });
            }
        }
    }
}

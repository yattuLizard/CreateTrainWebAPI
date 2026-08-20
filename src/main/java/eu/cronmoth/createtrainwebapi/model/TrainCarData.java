package eu.cronmoth.createtrainwebapi.model;

import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.nbt.CompoundTag;

import java.lang.reflect.Field;

public class TrainCarData {
    public int id;
    public double positionOnTrack;
    public String assemblyDirection;

    public int node1;
    public int node2;

    public double trailingPositionOnTrack;
    public int node3;
    public int node4;

    public TrainCarData(Carriage carriage) {
        id = carriage.id;

        var leadingPoint = carriage.getLeadingPoint();
        var trailingPoint = carriage.getTrailingPoint();

        if (leadingPoint == null || trailingPoint == null
                || leadingPoint.node1 == null || leadingPoint.node2 == null
                || trailingPoint.node1 == null || trailingPoint.node2 == null) {
            throw new TrackPositionUnavailableException(carriage.id);
        }

        positionOnTrack = leadingPoint.position;
        node1 = leadingPoint.node1.getNetId();
        node2 = leadingPoint.node2.getNetId();

        trailingPositionOnTrack = trailingPoint.position;
        node3 = trailingPoint.node1.getNetId();
        node4 = trailingPoint.node2.getNetId();

        try {
            Field f = Carriage.class.getDeclaredField("serialisedEntity");
            f.setAccessible(true);
            CompoundTag serialisedEntity = (CompoundTag) f.get(carriage);
            CompoundTag contraptionTag = serialisedEntity.getCompound("Contraption");
            assemblyDirection = contraptionTag.getString("AssemblyDirection");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static class TrackPositionUnavailableException extends RuntimeException {
        public TrackPositionUnavailableException(int carriageId) {
            super("Track position is unavailable for carriage " + carriageId);
        }
    }
}

package eu.cronmoth.createtrainwebapi.model;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.graph.TrackNode;
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
        positionOnTrack = carriage.getLeadingPoint().position;
        node1= carriage.getLeadingPoint().node1.getNetId();
        node2= carriage.getLeadingPoint().node2.getNetId();

        trailingPositionOnTrack = carriage.getTrailingPoint().position;
        node3= carriage.getTrailingPoint().node1.getNetId();
        node4= carriage.getTrailingPoint().node2.getNetId();

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
}
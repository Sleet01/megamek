package megamek.common.units;

public class ObscuredEntity implements IContact {

    private Entity entity;
    private int forcesLevel;
    private int positionLevel;
    private int logisticsLevel;
    private int personnelLevel;

    public ObscuredEntity(
          Entity entity, int forcesLevel, int positionLevel, int logisticsLevel, int personnelLevel
    ) {
        this.entity = entity;
        this.forcesLevel = forcesLevel;
        this.positionLevel = positionLevel;
        this.logisticsLevel = logisticsLevel;
        this.personnelLevel = personnelLevel;
    }

    public Entity getEntity() {
        return entity;
    }

    public Crew getCrew() {
        return entity.getCrew();
    }

    public String getShortName() {
        return entity.getShortName();
    }
}

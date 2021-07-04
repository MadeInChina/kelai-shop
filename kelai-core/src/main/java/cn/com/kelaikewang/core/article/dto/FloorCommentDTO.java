package cn.com.kelaikewang.core.article.dto;

public class FloorCommentDTO extends TrunkCommentDTO {
    private Integer floorNumber;
    private Long floorId;
    private Long trunkCommentId;
    private FloorCommentDTO nextFloor;

    public FloorCommentDTO getNextFloor() {
        return nextFloor;
    }

    public void setNextFloor(FloorCommentDTO nextFloor) {
        this.nextFloor = nextFloor;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getTrunkCommentId() {
        return trunkCommentId;
    }

    public void setTrunkCommentId(Long trunkCommentId) {
        this.trunkCommentId = trunkCommentId;
    }
}

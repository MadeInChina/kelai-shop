package cn.com.kelaikewang.core.article.dto;

import java.io.Serializable;

public class TrunkCommentDTO implements Serializable {

    private Long commentId;
    private String comment;
    private String commentDate;
    private Integer voteUp;
    private Integer voteDown;
    private String nickname;
    private String profilePhoto;
    private Long userId;

    private FloorCommentDTO floorComment;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getVoteUp() {
        return voteUp;
    }

    public void setVoteUp(Integer voteUp) {
        this.voteUp = voteUp;
    }

    public Integer getVoteDown() {
        return voteDown;
    }

    public void setVoteDown(Integer voteDown) {
        this.voteDown = voteDown;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public FloorCommentDTO getFloorComment() {
        return floorComment;
    }

    public void setFloorComment(FloorCommentDTO floorComment) {
        this.floorComment = floorComment;
    }
}

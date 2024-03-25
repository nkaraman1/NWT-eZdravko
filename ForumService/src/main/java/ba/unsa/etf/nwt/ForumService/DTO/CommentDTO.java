package ba.unsa.etf.nwt.ForumService.DTO;

public class CommentDTO {
    private Long questionId;
    private String userUid;
    private String sadrzaj;
    private Integer anonimnost;

    public CommentDTO(Long questionId, String userUid, String sadrzaj, Integer anonimnost) {
        this.questionId = questionId;
        this.userUid = userUid;
        this.sadrzaj = sadrzaj;
        this.anonimnost = anonimnost;
    }

    public CommentDTO() {

    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public Integer getAnonimnost() {
        return anonimnost;
    }

    public void setAnonimnost(Integer anonimnost) {
        this.anonimnost = anonimnost;
    }
}

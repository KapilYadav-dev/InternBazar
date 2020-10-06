package in.kay.internbazar.Model;

public class InternshipModel {
    String _id, location, skillsReq, title, description, stipend, internshipPeriod, companyName, internshipType, applyBy, startDate, whocanApply, perks, __v;
    Integer vacancy;

    public InternshipModel() {
    }

    public InternshipModel(String _id, String location, String skillsReq, String title, String description, String stipend, String internshipPeriod, String companyName, String internshipType, String applyBy, String startDate, String whocanApply, String perks, String __v, Integer vacancy) {
        this._id = _id;
        this.location = location;
        this.skillsReq = skillsReq;
        this.title = title;
        this.description = description;
        this.stipend = stipend;
        this.internshipPeriod = internshipPeriod;
        this.companyName = companyName;
        this.internshipType = internshipType;
        this.applyBy = applyBy;
        this.startDate = startDate;
        this.whocanApply = whocanApply;
        this.perks = perks;
        this.__v = __v;
        this.vacancy = vacancy;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSkillsReq() {
        return skillsReq;
    }

    public void setSkillsReq(String skillsReq) {
        this.skillsReq = skillsReq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStipend() {
        return stipend;
    }

    public void setStipend(String stipend) {
        this.stipend = stipend;
    }

    public String getInternshipPeriod() {
        return internshipPeriod;
    }

    public void setInternshipPeriod(String internshipPeriod) {
        this.internshipPeriod = internshipPeriod;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInternshipType() {
        return internshipType;
    }

    public void setInternshipType(String internshipType) {
        this.internshipType = internshipType;
    }

    public String getApplyBy() {
        return applyBy;
    }

    public void setApplyBy(String applyBy) {
        this.applyBy = applyBy;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getWhocanApply() {
        return whocanApply;
    }

    public void setWhocanApply(String whocanApply) {
        this.whocanApply = whocanApply;
    }

    public String getPerks() {
        return perks;
    }

    public void setPerks(String perks) {
        this.perks = perks;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public Integer getVacancy() {
        return vacancy;
    }

    public void setVacancy(Integer vacancy) {
        this.vacancy = vacancy;
    }
}

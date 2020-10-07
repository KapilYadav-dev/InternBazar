package in.kay.internbazar.Model;

public class ApplicationModel {

    public ApplicationModel(String companyName, String internshipProfile, String status, String internshipId) {
        this.companyName = companyName;
        this.internshipProfile = internshipProfile;
        this.status = status;
        this.internshipId = internshipId;
    }

    String companyName, internshipProfile, status, internshipId;

    public String getCompanyName() {
        return companyName;
    }

    public String getInternshipProfile() {
        return internshipProfile;
    }

    public String getStatus() {
        return status;
    }

    public String getInternshipId() {
        return internshipId;
    }
}

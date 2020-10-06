package in.kay.internbazar.Model;

public class SliderItem {

    private String description;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    private String title;


    public SliderItem(String description, String imageUrl,String title) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

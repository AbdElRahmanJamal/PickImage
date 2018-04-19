package example.com.saveimage.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageData implements Serializable {
    @SerializedName("encoded_string")
    @Expose
    String encoded_string;
    @SerializedName("image_name")
    @Expose
    String image_name;

    public String getName() {
        return encoded_string;
    }

    public void setName(String encoded_string) {
        this.encoded_string = encoded_string;
    }

    public String getId() {
        return image_name;
    }

    public void setId(String image_name) {
        this.image_name = image_name;
    }

    public ImageData(String encoded_string, String image_name) {

        this.encoded_string = encoded_string;
        this.image_name = image_name;
    }
}

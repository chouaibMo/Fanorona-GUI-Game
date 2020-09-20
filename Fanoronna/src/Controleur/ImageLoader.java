package Controleur;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoader {
    public String folderpath;
    
    public ImageLoader(String fp){
        this.folderpath = fp;
    }

    public Image loadImage(String filename){
        InputStream is = getClass().getResourceAsStream(folderpath+filename);
        Image img = new Image(is);
        return img;
    }
    
    public ImageView loadImageView(String filename){
        InputStream is = getClass().getResourceAsStream(folderpath+filename);
        ImageView iv = new ImageView(new Image(is));
        return iv;
    }

}

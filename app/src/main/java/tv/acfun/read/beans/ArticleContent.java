package tv.acfun.read.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ArticleContent {
    private String content;
    private boolean image;

    public String getContent() {

        return content;
    }

    public boolean isImage() {
        return image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(boolean image) {
        this.image = image;
    }
}
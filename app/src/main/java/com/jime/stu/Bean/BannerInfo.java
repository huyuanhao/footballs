package com.jime.stu.Bean;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

/**
 * @author PC
 * @date 2020/05/25 14:04
 */
public class BannerInfo implements BaseBannerInfo {
    private String url;
    private int img;

    public BannerInfo(int img){
        this.img = img;
    }
    @Override
    public Object getXBannerUrl() {
        return url;
    }

    @Override
    public String getXBannerTitle() {
        return url;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

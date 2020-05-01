package com.e.ekanektask.model;

import java.util.List;
public class ImagesModel {
    private List<HitsBean> hits;
    public List<HitsBean> getHits() {
        return hits;
    }
    public void setHits(List<HitsBean> hits) {
        this.hits = hits;
    }




    public static class HitsBean {
        private String webformatURL;
        public String getWebformatURL() {
            return webformatURL;
        }
        public void setWebformatURL(String webformatURL) {
            this.webformatURL = webformatURL;
        }
    }
}

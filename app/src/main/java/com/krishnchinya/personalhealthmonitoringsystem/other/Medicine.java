package com.krishnchinya.personalhealthmonitoringsystem.other;

import com.google.api.client.util.NullValue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.http.Field;

/**
 * Created by KrishnChinya on 3/26/17.
 */

public class Medicine {

    @SerializedName("data")
    public List<data> datainfo;

    @SerializedName("metadata")
    public meta metainfo;

    public List<data> getDatainfo() {
        return datainfo;
    }

    public void setDatainfo(List<data> datainfo) {
        this.datainfo = datainfo;
    }

    public meta getMetainfo() {
        return metainfo;
    }

    public void setMetainfo(meta metainfo) {
        this.metainfo = metainfo;
    }

    public static final class data {
        @SerializedName("spl_version")
        String version;
       @SerializedName("published_date")
        String publishdate;
       @SerializedName("title")
        String title;
       @SerializedName("setid")
        String id;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getPublishdate() {
            return publishdate;
        }

        public void setPublishdate(String publishdate) {
            this.publishdate = publishdate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static final class meta{
        @SerializedName("db_published_date")
        String publisheddate;
        @SerializedName("elements_per_page")
        String elementsperpage;
        @SerializedName("current_url")
        String currurl;
        @SerializedName("next_page_url")
        String nexturl;
        @SerializedName("total_elements")
        String totalele;
        @SerializedName("total_pages")
        String totalpages;
        @SerializedName("current_page")
        String currpage;
        @SerializedName("previous_page")
        String previouspage;
        @SerializedName("previous_page_url")
        String prepageurl;
        @SerializedName("next_page")
        String nextpage;

        public String getPublisheddate() {
            return publisheddate;
        }

        public void setPublisheddate(String publisheddate) {
            this.publisheddate = publisheddate;
        }

        public String getElementsperpage() {
            return elementsperpage;
        }

        public void setElementsperpage(String elementsperpage) {
            this.elementsperpage = elementsperpage;
        }

        public String getCurrurl() {
            return currurl;
        }

        public void setCurrurl(String currurl) {
            this.currurl = currurl;
        }

        public String getNexturl() {
            return nexturl;
        }

        public void setNexturl(String nexturl) {
            this.nexturl = nexturl;
        }

        public String getTotalele() {
            return totalele;
        }

        public void setTotalele(String totalele) {
            this.totalele = totalele;
        }

        public String getTotalpages() {
            return totalpages;
        }

        public void setTotalpages(String totalpages) {
            this.totalpages = totalpages;
        }

        public String getCurrpage() {
            return currpage;
        }

        public void setCurrpage(String currpage) {
            this.currpage = currpage;
        }

        public String getPreviouspage() {
            return previouspage;
        }

        public void setPreviouspage(String previouspage) {
            this.previouspage = previouspage;
        }

        public String getPrepageurl() {
            return prepageurl;
        }

        public void setPrepageurl(String prepageurl) {
            this.prepageurl = prepageurl;
        }

        public String getNextpage() {
            return nextpage;
        }

        public void setNextpage(String nextpage) {
            this.nextpage = nextpage;
        }
    }

}

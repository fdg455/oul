package com.oushangfeng.ounews.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ClassName: NeteastNewsSummary<p>
 * Author:oubowu<p>
 * Fuction: 网易新闻详情<p>
 * CreateDate:2016/2/13 20:09<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeteastNewsSummary {
    @JsonProperty("postid")
    public String postid;
    @JsonProperty("hasCover")
    public boolean hasCover;
    @JsonProperty("hasHead")
    public int hasHead;
    @JsonProperty("replyCount")
    public int replyCount;
    @JsonProperty("hasImg")
    public int hasImg;
    @JsonProperty("digest")
    public String digest;
    @JsonProperty("hasIcon")
    public boolean hasIcon;
    @JsonProperty("docid")
    public String docid;
    @JsonProperty("title")
    public String title;
    @JsonProperty("order")
    public int order;
    @JsonProperty("priority")
    public int priority;
    @JsonProperty("lmodify")
    public String lmodify;
    @JsonProperty("boardid")
    public String boardid;
    @JsonProperty("photosetID")
    public String photosetID;
    @JsonProperty("template")
    public String template;
    @JsonProperty("votecount")
    public int votecount;
    @JsonProperty("skipID")
    public String skipID;
    @JsonProperty("alias")
    public String alias;
    @JsonProperty("skipType")
    public String skipType;
    @JsonProperty("cid")
    public String cid;
    @JsonProperty("hasAD")
    public int hasAD;
    @JsonProperty("imgsrc")
    public String imgsrc;
    @JsonProperty("tname")
    public String tname;
    @JsonProperty("ename")
    public String ename;
    @JsonProperty("ptime")
    public String ptime;
    /**
     * title : 哈萨克斯坦中亚在建第1高楼爆炸起火
     * tag : photoset
     * imgsrc : http://img5.cache.netease.com/3g/2016/2/13/2016021318005710210.jpg
     * subtitle :
     * url : 00AN0001|110630
     */

    @JsonProperty("ads")
    public List<AdsEntity> ads;
    /**
     * imgsrc : http://img5.cache.netease.com/3g/2016/2/13/201602131446132dc50.jpg
     */

    @JsonProperty("imgextra")
    public List<ImgextraEntity> imgextra;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdsEntity {
        @JsonProperty("title")
        public String title;
        @JsonProperty("tag")
        public String tag;
        @JsonProperty("imgsrc")
        public String imgsrc;
        @JsonProperty("subtitle")
        public String subtitle;
        @JsonProperty("url")
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImgextraEntity {
        @JsonProperty("imgsrc")
        public String imgsrc;
    }
}

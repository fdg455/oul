package com.oushangfeng.ounews.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ClassName: SinaPhotoList<p>
 * Author:oubowu<p>
 * Fuction: 新浪图片列表<p>
 * CreateDate:2016/2/14 0:36<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SinaPhotoList {

    @JsonProperty("status")
    public int status;

    @JsonProperty("data")
    public DataEntity data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataEntity {
        @JsonProperty("is_intro")
        public String isIntro;

        @JsonProperty("list")
        public List<PhotoListEntity> list;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PhotoListEntity {

            /**
             * 自己加的记录测出来的宽高
             */
            public int picWidth = -1;
            public int picHeight = -1;

            @JsonProperty("id")
            public String id;
            @JsonProperty("title")
            public String title;
            @JsonProperty("long_title")
            public String longTitle;
            @JsonProperty("source")
            public String source;
            @JsonProperty("link")
            public String link;
            @JsonProperty("pic")
            public String pic;
            @JsonProperty("kpic")
            public String kpic;
            @JsonProperty("bpic")
            public String bpic;
            @JsonProperty("intro")
            public String intro;
            @JsonProperty("pubDate")
            public int pubDate;
            @JsonProperty("articlePubDate")
            public int articlePubDate;
            @JsonProperty("comments")
            public String comments;

            @JsonProperty("pics")
            public PicsEntity pics;
            @JsonProperty("feedShowStyle")
            public String feedShowStyle;
            @JsonProperty("category")
            public String category;
            @JsonProperty("comment")
            public int comment;
            /**
             * qreply : 28
             * total : 64
             * show : 5
             * comment_status : 0
             * praise : 20
             * dispraise : 5
             */

            @JsonProperty("comment_count_info")
            public CommentCountInfoEntity commentCountInfo;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class PicsEntity {
                @JsonProperty("total")
                public int total;
                @JsonProperty("picTemplate")
                public int picTemplate;
                /**
                 * pic : http://r3.sinaimg.cn/10230/2016/0212/27/7/77567220/auto.jpg
                 * alt : 米兰达可儿
                 * kpic : http://l.sinaimg.cn/www/dy/slidenews/4_img/2016_06/704_1849131_634459.jpg/original.jpg
                 */

                @JsonProperty("list")
                public List<ListEntity> list;

                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class ListEntity {
                    @JsonProperty("pic")
                    public String pic;
                    @JsonProperty("alt")
                    public String alt;
                    @JsonProperty("kpic")
                    public String kpic;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class CommentCountInfoEntity {
                @JsonProperty("qreply")
                public int qreply;
                @JsonProperty("total")
                public int total;
                @JsonProperty("show")
                public int show;
                @JsonProperty("comment_status")
                public int commentStatus;
                @JsonProperty("praise")
                public int praise;
                @JsonProperty("dispraise")
                public int dispraise;
            }
        }
    }
}

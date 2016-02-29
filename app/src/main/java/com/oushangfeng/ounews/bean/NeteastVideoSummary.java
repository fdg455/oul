package com.oushangfeng.ounews.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ClassName: NeteastVideoSummary<p>
 * Author:oubowu<p>
 * Fuction: 网易视频列表<p>
 * CreateDate:2016/2/14 0:39<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeteastVideoSummary {

    /**
     * 自己加的记录测出来的宽高
     */
    public int picWidth = -1;
    public int picHeight = -1;

    /**
     * replyCount : 895
     * videosource : 新媒体
     * mp4Hd_url : null
     * cover : http://vimg2.ws.126.net/image/snapshot/2016/2/5/H/VBEMM3F5H.jpg
     * title : 渥太华600人欢乐春节快闪
     * playCount : 22751
     * replyBoard : video_bbs
     * sectiontitle :
     * description : 超过600人在-11度的天气下庆祝春节！
     * replyid : BEML6HHI008535RB
     * mp4_url : http://flv2.bn.netease.com/videolib3/1602/12/BFihm9923/SD/BFihm9923-mobile.mp4
     * length : 666
     * playersize : 1
     * m3u8Hd_url : null
     * vid : VBEML6HHI
     * m3u8_url : http://flv2.bn.netease.com/videolib3/1602/12/BFihm9923/SD/movie_index.m3u8
     * ptime : 2016-02-12 12:05:39
     */

    @JsonProperty("replyCount")
    public int replyCount;
    @JsonProperty("videosource")
    public String videosource;
    @JsonProperty("mp4Hd_url")
    public Object mp4HdUrl;
    @JsonProperty("cover")
    public String cover;
    @JsonProperty("title")
    public String title;
    @JsonProperty("playCount")
    public int playCount;
    @JsonProperty("replyBoard")
    public String replyBoard;
    @JsonProperty("sectiontitle")
    public String sectiontitle;
    @JsonProperty("description")
    public String description;
    @JsonProperty("replyid")
    public String replyid;
    @JsonProperty("mp4_url")
    public String mp4Url;
    @JsonProperty("length")
    public int length;
    @JsonProperty("playersize")
    public int playersize;
    @JsonProperty("m3u8Hd_url")
    public Object m3u8HdUrl;
    @JsonProperty("vid")
    public String vid;
    @JsonProperty("m3u8_url")
    public String m3u8Url;
    @JsonProperty("ptime")
    public String ptime;
}

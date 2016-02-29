package com.oushangfeng.ounews.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: SinaPhotoDetail<p>
 * Author:oubowu<p>
 * Fuction: 新浪图片详情<p>
 * CreateDate:2016/2/14 0:38<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SinaPhotoDetail implements Serializable {


    /**
     * status : 0
     * data : {"id":"20550-66955-hdpic","title":"佳能1DX Mark II图赏","long_title":"佳能EOS 1DX Mark II官方真机+上手图赏","source":"新浪","link":"http://photo.sina.cn/album_5_20550_66955.htm?fromsinago=1","comments":"slidenews-album-20550-66955_1_kj","need_match_pic":true,"pubDate":1454453982,"keys":[],"lead":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","content":"<br/><!--{IMG_1}--><br/><!--{IMG_2}--><br/><!--{IMG_3}--><br/><!--{IMG_4}--><br/><!--{IMG_5}--><br/><!--{IMG_6}--><br/><!--{IMG_7}--><br/><!--{IMG_8}--><br/><!--{IMG_9}--><br/><!--{IMG_10}--><br/><!--{IMG_11}--><br/><!--{IMG_12}--><br/><!--{IMG_13}--><br/><!--{IMG_14}--><br/><!--{IMG_15}--><br/><!--{IMG_16}--><br/><!--{IMG_17}--><br/><!--{IMG_18}--><br/><!--{IMG_19}--><br/><!--{IMG_20}--><br/><!--{IMG_21}--><br/><!--{IMG_22}--><br/><!--{IMG_23}--><br/><!--{IMG_24}--><br/><!--{IMG_25}--><br/><!--{IMG_26}--><br/><!--{IMG_27}--><br/><!--{IMG_28}--><br/><!--{IMG_29}--><br/><!--{IMG_30}--><br/><!--{IMG_31}--><br/><!--{IMG_32}--><br/><!--{IMG_33}--><br/>佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","videos":[],"pics":[{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ad/0/92564383/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329357_347462.jpg/original.jpg","size":"950x713"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/d6/1/91564250/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329358_876618.jpg/original.jpg","size":"950x713"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/0f/1/09564978/auto.jpg","alt":"佳能EOS 1DX Mark II肩屏以及按键特写（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329359_529861.jpg/original.jpg","size":"950x713"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ea/8/25571150/auto.jpg","alt":"佳能EOS 1DX Mark II左肩按键（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329360_311527.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/2e/0/72565737/auto.jpg","alt":"佳能EOS 1DX Mark II背部按键以及屏幕（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329361_965513.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/84/6/44566630/auto.jpg","alt":"佳能EOS 1DX Mark II背部按键以及屏幕（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329362_606460.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/63/d/42572538/auto.jpg","alt":"佳能EOS 1DX Mark II存储卡卡槽，一个为CF，一个为CFast（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329363_185008.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/30/8/26572289/auto.jpg","alt":"佳能EOS 1DX Mark II背部按键以及屏幕（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329364_417260.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/c6/9/77571061/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329365_383486.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/85/5/73565908/auto.jpg","alt":"佳能EOS 1DX Mark II有线Lan口（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329366_813309.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/e6/9/50571815/auto.jpg","alt":"佳能EOS 1DX Mark II接口特写（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329367_232529.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/73/9/25571227/auto.jpg","alt":"佳能EOS 1DX Mark II电池（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329368_927152.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/c5/1/56564807/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329369_475976.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/7b/9/17570410/auto.jpg","alt":"佳能EOS 1DX Mark II未装载镜头效果（图片来源dprivew）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329370_164784.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/90/e/56570630/auto.jpg","alt":"佳能EOS 1DX Mark II菜单（图片来源dprivew）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329371_575696.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/e7/2/83566349/auto.jpg","alt":"佳能EOS 1DX Mark II正面图","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329372_760757.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/25/5/42565127/auto.jpg","alt":"佳能EOS 1DX Mark II正面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329373_467492.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/85/c/91571879/auto.jpg","alt":"佳能EOS 1DX Mark II背部","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329374_247953.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ff/8/14570743/auto.jpg","alt":"佳能EOS 1DX Mark II背面图","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329375_357418.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/2d/9/43571557/auto.jpg","alt":"佳能EOS 1DX Mark II侧面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329376_384355.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/f4/c/55570914/auto.jpg","alt":"佳能EOS 1DX MarkII侧面接口","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329377_884366.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/36/7/11565860/auto.jpg","alt":"佳能EOS 1DX Mark II顶部","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329378_577492.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/8f/7/30564866/auto.jpg","alt":"佳能EOS 1DX Mark II侧面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329379_457469.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/18/b/72570841/auto.jpg","alt":"佳能EOS 1DX Mark II斜侧图","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329380_826948.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/43/c/92571204/auto.jpg","alt":"佳能EOS 1DX Mark II肩屏特写","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329381_330208.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ac/c/83573244/auto.jpg","alt":"佳能EOS 1DX Mark II背面按键特写","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329382_917840.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/0f/6/31566627/auto.jpg","alt":"佳能EOS 1DX Mark II双卡槽设计","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329383_936850.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/33/8/80572458/auto.jpg","alt":"佳能EOS 1DX Mark II正面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329384_213684.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/06/a/69572265/auto.jpg","alt":"佳能EOS 1DX Mark II斜侧面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329385_790227.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/eb/1/25565503/auto.jpg","alt":"佳能EOS 1DX Mark II正面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329386_554008.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/45/0/10564579/auto.jpg","alt":"佳能EOS 1DX Mark II电池充电器","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329387_180566.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/81/6/90567999/auto.jpg","alt":"佳能EOS 1DX Mark II包装清单","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329388_742300.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/d1/5/74566091/auto.jpg","alt":"佳能EOS 1DX Mark II电池","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329389_326632.jpg/original.jpg"}],"recommends":[]}
     */

    @JsonProperty("status")
    public int status;
    /**
     * id : 20550-66955-hdpic
     * title : 佳能1DX Mark II图赏
     * long_title : 佳能EOS 1DX Mark II官方真机+上手图赏
     * source : 新浪
     * link : http://photo.sina.cn/album_5_20550_66955.htm?fromsinago=1
     * comments : slidenews-album-20550-66955_1_kj
     * need_match_pic : true
     * pubDate : 1454453982
     * keys : []
     * lead : 佳能EOS 1DX Mark II手持效果图（图片来源dpreview）
     * content : <br/><!--{IMG_1}--><br/><!--{IMG_2}--><br/><!--{IMG_3}--><br/><!--{IMG_4}--><br/><!--{IMG_5}--><br/><!--{IMG_6}--><br/><!--{IMG_7}--><br/><!--{IMG_8}--><br/><!--{IMG_9}--><br/><!--{IMG_10}--><br/><!--{IMG_11}--><br/><!--{IMG_12}--><br/><!--{IMG_13}--><br/><!--{IMG_14}--><br/><!--{IMG_15}--><br/><!--{IMG_16}--><br/><!--{IMG_17}--><br/><!--{IMG_18}--><br/><!--{IMG_19}--><br/><!--{IMG_20}--><br/><!--{IMG_21}--><br/><!--{IMG_22}--><br/><!--{IMG_23}--><br/><!--{IMG_24}--><br/><!--{IMG_25}--><br/><!--{IMG_26}--><br/><!--{IMG_27}--><br/><!--{IMG_28}--><br/><!--{IMG_29}--><br/><!--{IMG_30}--><br/><!--{IMG_31}--><br/><!--{IMG_32}--><br/><!--{IMG_33}--><br/>佳能EOS 1DX Mark II手持效果图（图片来源dpreview）
     * videos : []
     * pics : [{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ad/0/92564383/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329357_347462.jpg/original.jpg","size":"950x713"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/d6/1/91564250/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329358_876618.jpg/original.jpg","size":"950x713"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/0f/1/09564978/auto.jpg","alt":"佳能EOS 1DX Mark II肩屏以及按键特写（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329359_529861.jpg/original.jpg","size":"950x713"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ea/8/25571150/auto.jpg","alt":"佳能EOS 1DX Mark II左肩按键（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329360_311527.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/2e/0/72565737/auto.jpg","alt":"佳能EOS 1DX Mark II背部按键以及屏幕（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329361_965513.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/84/6/44566630/auto.jpg","alt":"佳能EOS 1DX Mark II背部按键以及屏幕（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329362_606460.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/63/d/42572538/auto.jpg","alt":"佳能EOS 1DX Mark II存储卡卡槽，一个为CF，一个为CFast（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329363_185008.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/30/8/26572289/auto.jpg","alt":"佳能EOS 1DX Mark II背部按键以及屏幕（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329364_417260.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/c6/9/77571061/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329365_383486.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/85/5/73565908/auto.jpg","alt":"佳能EOS 1DX Mark II有线Lan口（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329366_813309.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/e6/9/50571815/auto.jpg","alt":"佳能EOS 1DX Mark II接口特写（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329367_232529.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/73/9/25571227/auto.jpg","alt":"佳能EOS 1DX Mark II电池（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329368_927152.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/c5/1/56564807/auto.jpg","alt":"佳能EOS 1DX Mark II手持效果图（图片来源dpreview）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329369_475976.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/7b/9/17570410/auto.jpg","alt":"佳能EOS 1DX Mark II未装载镜头效果（图片来源dprivew）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329370_164784.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/90/e/56570630/auto.jpg","alt":"佳能EOS 1DX Mark II菜单（图片来源dprivew）","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329371_575696.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/e7/2/83566349/auto.jpg","alt":"佳能EOS 1DX Mark II正面图","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329372_760757.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/25/5/42565127/auto.jpg","alt":"佳能EOS 1DX Mark II正面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329373_467492.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/85/c/91571879/auto.jpg","alt":"佳能EOS 1DX Mark II背部","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329374_247953.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ff/8/14570743/auto.jpg","alt":"佳能EOS 1DX Mark II背面图","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329375_357418.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/2d/9/43571557/auto.jpg","alt":"佳能EOS 1DX Mark II侧面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329376_384355.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/f4/c/55570914/auto.jpg","alt":"佳能EOS 1DX MarkII侧面接口","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329377_884366.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/36/7/11565860/auto.jpg","alt":"佳能EOS 1DX Mark II顶部","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329378_577492.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/8f/7/30564866/auto.jpg","alt":"佳能EOS 1DX Mark II侧面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329379_457469.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/18/b/72570841/auto.jpg","alt":"佳能EOS 1DX Mark II斜侧图","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329380_826948.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/43/c/92571204/auto.jpg","alt":"佳能EOS 1DX Mark II肩屏特写","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329381_330208.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/ac/c/83573244/auto.jpg","alt":"佳能EOS 1DX Mark II背面按键特写","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329382_917840.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/0f/6/31566627/auto.jpg","alt":"佳能EOS 1DX Mark II双卡槽设计","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329383_936850.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/33/8/80572458/auto.jpg","alt":"佳能EOS 1DX Mark II正面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329384_213684.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/06/a/69572265/auto.jpg","alt":"佳能EOS 1DX Mark II斜侧面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329385_790227.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/eb/1/25565503/auto.jpg","alt":"佳能EOS 1DX Mark II正面","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329386_554008.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/45/0/10564579/auto.jpg","alt":"佳能EOS 1DX Mark II电池充电器","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329387_180566.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/81/6/90567999/auto.jpg","alt":"佳能EOS 1DX Mark II包装清单","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329388_742300.jpg/original.jpg"},{"pic":"http://r3.sinaimg.cn/10230/2016/0203/d1/5/74566091/auto.jpg","alt":"佳能EOS 1DX Mark II电池","kpic":"http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329389_326632.jpg/original.jpg"}]
     * recommends : []
     */

    @JsonProperty("data")
    public SinaPhotoDetailDataEntity data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SinaPhotoDetailDataEntity implements Serializable {
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
        @JsonProperty("comments")
        public String comments;
        @JsonProperty("need_match_pic")
        public boolean needMatchPic;
        @JsonProperty("pubDate")
        public int pubDate;
        @JsonProperty("lead")
        public String lead;
        @JsonProperty("content")
        public String content;
        @JsonProperty("keys")
        public List<?> keys;
        @JsonProperty("videos")
        public List<?> videos;
        /**
         * pic : http://r3.sinaimg.cn/10230/2016/0203/ad/0/92564383/auto.jpg
         * alt : 佳能EOS 1DX Mark II手持效果图（图片来源dpreview）
         * kpic : http://l.sinaimg.cn/www/dy/slidenews/5_img/2016_05/20550_1329357_347462.jpg/original.jpg
         * size : 950x713
         */

        @JsonProperty("pics")
        public List<SinaPhotoDetailPicsEntity> pics;
        @JsonProperty("recommends")
        public List<?> recommends;


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SinaPhotoDetailPicsEntity implements Serializable {
        @JsonProperty("pic")
        public String pic;
        @JsonProperty("alt")
        public String alt;
        @JsonProperty("kpic")
        public String kpic;
        @JsonProperty("size")
        public String size;

        /**
         * 自己加的，标志现在的图片是否被拉大了，用于控制其它信息的隐藏显示
         */
        public boolean showTitle = true;

    }

}

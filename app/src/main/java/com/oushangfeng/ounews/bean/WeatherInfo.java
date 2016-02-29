package com.oushangfeng.ounews.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ClassName: WeatherInfo<p>
 * Author:oubowu<p>
 * Fuction: 天气查询信息<p>
 * CreateDate:2016/2/14 0:41<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherInfo {


    /**
     * desc : OK
     * status : 1000
     * data : {"wendu":"-5","ganmao":"澶╁喎椋庡ぇ锛屾槗鍙戠敓鎰熷啋锛岃娉ㄦ剰閫傚綋澧炲姞琛ｆ湇锛屽姞寮鸿嚜鎴戦槻鎶ら伩鍏嶆劅鍐掋\u20ac�","forecast":[{"fengxiang":"鍖楅","fengli":"5-6绾�","high":"楂樻俯 4鈩�","type":"闃�","low":"浣庢俯 -5鈩�","date":"14鏃ユ槦鏈熷ぉ"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"4-5绾�","high":"楂樻俯 1鈩�","type":"鏅�","low":"浣庢俯 -5鈩�","date":"15鏃ユ槦鏈熶竴"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 4鈩�","type":"鏅�","low":"浣庢俯 -3鈩�","date":"16鏃ユ槦鏈熶簩"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 8鈩�","type":"鏅�","low":"浣庢俯 -2鈩�","date":"17鏃ユ槦鏈熶笁"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 10鈩�","type":"鏅�","low":"浣庢俯 -2鈩�","date":"18鏃ユ槦鏈熷洓"}],"yesterday":{"fl":"5-6绾�","fx":"鍖楅","high":"楂樻俯 4鈩�","type":"闃�","low":"浣庢俯 -6鈩�","date":"13鏃ユ槦鏈熷叚"},"aqi":"35","city":"鍖椾含"}
     */

    @JsonProperty("desc")
    public String desc;
    @JsonProperty("status")
    public int status;
    /**
     * wendu : -5
     * ganmao : 澶╁喎椋庡ぇ锛屾槗鍙戠敓鎰熷啋锛岃娉ㄦ剰閫傚綋澧炲姞琛ｆ湇锛屽姞寮鸿嚜鎴戦槻鎶ら伩鍏嶆劅鍐掋€�
     * forecast : [{"fengxiang":"鍖楅","fengli":"5-6绾�","high":"楂樻俯 4鈩�","type":"闃�","low":"浣庢俯 -5鈩�","date":"14鏃ユ槦鏈熷ぉ"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"4-5绾�","high":"楂樻俯 1鈩�","type":"鏅�","low":"浣庢俯 -5鈩�","date":"15鏃ユ槦鏈熶竴"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 4鈩�","type":"鏅�","low":"浣庢俯 -3鈩�","date":"16鏃ユ槦鏈熶簩"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 8鈩�","type":"鏅�","low":"浣庢俯 -2鈩�","date":"17鏃ユ槦鏈熶笁"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 10鈩�","type":"鏅�","low":"浣庢俯 -2鈩�","date":"18鏃ユ槦鏈熷洓"}]
     * yesterday : {"fl":"5-6绾�","fx":"鍖楅","high":"楂樻俯 4鈩�","type":"闃�","low":"浣庢俯 -6鈩�","date":"13鏃ユ槦鏈熷叚"}
     * aqi : 35
     * city : 鍖椾含
     */

    @JsonProperty("data")
    public DataEntity data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataEntity {

        @Override
        public String toString() {
            return "DataEntity{" +
                    "wendu='" + wendu + '\'' +
                    ", ganmao='" + ganmao + '\'' +
                    ", yesterday=" + yesterday +
                    ", aqi='" + aqi + '\'' +
                    ", city='" + city + '\'' +
                    ", forecast=" + forecast +
                    '}';
        }

        @JsonProperty("wendu")
        public String wendu;
        @JsonProperty("ganmao")
        public String ganmao;
        /**
         * fl : 5-6绾�
         * fx : 鍖楅
         * high : 楂樻俯 4鈩�
         * type : 闃�
         * low : 浣庢俯 -6鈩�
         * date : 13鏃ユ槦鏈熷叚
         */

        @JsonProperty("yesterday")
        public YesterdayEntity yesterday;
        @JsonProperty("aqi")
        public String aqi;
        @JsonProperty("city")
        public String city;
        /**
         * fengxiang : 鍖楅
         * fengli : 5-6绾�
         * high : 楂樻俯 4鈩�
         * type : 闃�
         * low : 浣庢俯 -5鈩�
         * date : 14鏃ユ槦鏈熷ぉ
         */

        @JsonProperty("forecast")
        public List<ForecastEntity> forecast;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class YesterdayEntity {

            @Override
            public String toString() {
                return "YesterdayEntity{" +
                        "fl='" + fl + '\'' +
                        ", fx='" + fx + '\'' +
                        ", high='" + high + '\'' +
                        ", type='" + type + '\'' +
                        ", low='" + low + '\'' +
                        ", date='" + date + '\'' +
                        '}';
            }

            @JsonProperty("fl")
            public String fl;
            @JsonProperty("fx")
            public String fx;
            @JsonProperty("high")
            public String high;
            @JsonProperty("type")
            public String type;
            @JsonProperty("low")
            public String low;
            @JsonProperty("date")
            public String date;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ForecastEntity {

            @Override
            public String toString() {
                return "ForecastEntity{" +
                        "fengxiang='" + fengxiang + '\'' +
                        ", fengli='" + fengli + '\'' +
                        ", high='" + high + '\'' +
                        ", type='" + type + '\'' +
                        ", low='" + low + '\'' +
                        ", date='" + date + '\'' +
                        '}';
            }

            @JsonProperty("fengxiang")
            public String fengxiang;
            @JsonProperty("fengli")
            public String fengli;
            @JsonProperty("high")
            public String high;
            @JsonProperty("type")
            public String type;
            @JsonProperty("low")
            public String low;
            @JsonProperty("date")
            public String date;
        }
    }
}

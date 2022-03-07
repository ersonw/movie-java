package com.telebott.moviejava.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class YzmData {
    private String metadata;
    private String tmpl;
    private String picdomain;
    private String infoHash;
    private String shareid;
    private String qrprefix;
    private String title;
    private String result;
    private int sp_status;
    private String mp4domain;
    private String rpath;
    private String path;
    private String orgfile;
    private String domain;
    private int progress;
    private String md5;
    private String output;
    private String category;
    private String outdir;

    public MetaData getMetadata(){
        return JSONObject.toJavaObject(JSONObject.parseObject(metadata),MetaData.class);
    }
    public OutPutData getOutput(){
        return JSONObject.toJavaObject(JSONObject.parseObject(output),OutPutData.class);
    }
}

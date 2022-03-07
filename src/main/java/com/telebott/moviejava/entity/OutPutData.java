package com.telebott.moviejava.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OutPutData {
    private String pic1;
    private String gif;
    private String video;
    public List<VideoData> getVideo() {
        return JSONArray.parseArray(video,VideoData.class);
    }
}

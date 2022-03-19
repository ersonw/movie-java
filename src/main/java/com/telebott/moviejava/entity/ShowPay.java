package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "show_pay")
@ToString(includeFieldNames = true)
@Setter
@Getter
public class ShowPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String domain;
    private String mchId;
    private String callbackUrl;
    private String notifyUrl;
    private String errorUrl;
    private String secretKey;
}

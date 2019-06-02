package com.cocoker.beans;

import com.cocoker.utils.serializer.DateFormatSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/23 4:00 PM
 * @Version: 1.0
 */
@Entity
@Table(name = "tbl_userinfo")
@DynamicUpdate
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.NONE,setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UserInfo implements Serializable{

    private static final long serialVersionUID = 6075951353743630339L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    @JsonProperty(value = "uid")
    private Integer yUid;

    @JsonProperty(value = "username")
    private String yUsername;

    @JsonProperty("upwd")
    private String yUpwd;

    @JsonProperty("otype")
    private Integer yOtype;

    @JsonProperty("ustatus")
    private Integer yUstatus;

    @JsonProperty("oid")
    private String yOid;

    @JsonProperty("upic")
    private String yUpic;

    @JsonProperty("usertype")
    private Integer yUsertype;

    @JsonProperty("openid")
    private String yOpenid;

    @JsonProperty("exchangeOpenid")
    private String yExchangeOpenid;

    @JsonProperty("nickname")
    private String yNickname;

    @JsonProperty("usermoney")
    private BigDecimal yUsermoney;

    @JsonProperty("updatetime")
    @JsonSerialize(using = DateFormatSerializer.class)
    private Date yUpdatetime;

    @JsonProperty("createtime")
    @JsonSerialize(using = DateFormatSerializer.class)
    private Date yCreatetime;

}

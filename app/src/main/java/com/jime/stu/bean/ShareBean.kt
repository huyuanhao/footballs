package com.jime.stu.bean

import com.jime.stu.utils.Preference
import java.io.Serializable

/**
 * 作者:zh
 * 时间:12/22/18 3:47 PM
 * 描述:
 */
class ShareBean : Serializable {
    var title by Preference(Preference.SHARETITLE,  "")
    var content  by Preference(Preference.SHARECONTENT,  "")
    var url  by Preference(Preference.SHAREURL,  "")
    var phone = ""
    var sms = ""
    //    public String getBaseUrl (){
//        String baseurl = SplashInfoUtil.getBaseShareUrl();
//       String shareUrl =baseurl+"?invitorId="+ UserUtil.getUserId()+"&invitorChannel="+2;
//        return shareUrl;
//    }
    override fun toString(): String {
        return "ShareBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", sms='" + sms + '\'' +
                '}'
    }
}
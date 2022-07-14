package com.bsks.utils;

import com.bsks.api.result.Result;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;

import java.util.Random;

public class SendMessageUtil {

    // 短信应用SDK AppID
    private static String secretId = "AKIDjPownOilX4KJ1SdW8TYMs77qITbT4OQC";

    // 短信应用SDK AppKey
    private static String secretKey = "WliOownNgiUzECtJTMxHK7dspWw6zcy3";

    /**
     * 生成验证码
     * @param number 验证码位数
     * @return
     */
    public static String createCode(int number){
        String code = "";
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int r = random.nextInt(10);
            code = code + r;
        }
        return code;
    }

    /**
     *发送短信
     * @param phoneNumberSet 手机号码
     * @param templateId 模板id
     * @param signName 签名全称
     * @return
     */
    public static boolean sendMessage(String[] phoneNumberSet, String templateId, String[] templateParams, String signName) {
        try {

            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou",clientProfile);

            SendSmsRequest req = new SendSmsRequest();

            String sdkAppId = "1400652392";
            req.setSmsSdkAppId(sdkAppId);

            req.setSignName(signName);
            req.setTemplateId(templateId);

            req.setTemplateParamSet(templateParams);

            /* 下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]
             * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号 */
            for (String phoneNumber : phoneNumberSet) {
                phoneNumber = "+86" + phoneNumber;
            }
            req.setPhoneNumberSet(phoneNumberSet);

            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse res = client.SendSms(req);
            System.out.println(SendSmsResponse.toJsonString(res));
            for (SendStatus sendStatus : res.getSendStatusSet()) {
                if ("".equals(sendStatus.getSerialNo())){
                    return false;
                }
            }
            return true;

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.tj.common.sms;


import com.alibaba.fastjson.JSONObject;

public class SmsTest {
	public static void main(String[] args) {
		/*MovekParamsSmsClientSmsBean bean = new MovekParamsSmsClientSmsBean();
		bean.setMsg("{$var},你好！我注意你很久了！【长得帅有限公司】");
		bean.setParams("18113025608,韩女士");
		bean.setSendtime("2017-08-17 12:00:00");
		JSONObject sendSimpleSms = MovekSmsClient.sendParaSms(bean);
		System.out.println(sendSimpleSms);*/
		
		MovekSmsClientSmsBean bean = new MovekSmsClientSmsBean();
		bean.setMobile("15928517407");
		bean.setContent("尊敬的朋友：您好！宝龙商业特委托第三方对与宝龙合作的经营伙伴们进行招商、营运、物业三个方面的满意度回访，若您有时间请接听客服电话！如果您有其他的问题或者需要总部协调的问题，可在工作日拨打我们的投诉热线400-921-0287，我们竭诚为您服务，给您带来不便，尽情谅解！退订回T【宝龙地产】");
//		bean.setSendtime("2017-08-17 12:00:00");
		JSONObject sendSimpleSms = MovekSmsClient.sendSimpleSms(bean);
		System.out.println(sendSimpleSms);
	}
}

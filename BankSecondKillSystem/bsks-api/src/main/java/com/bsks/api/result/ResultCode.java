package com.bsks.api.result;

/**
 * 接口返回的编码与信息
 */
public enum ResultCode {

    Success(20000, "请求成功"),
    UnknownException(-1, "未知异常"),
    SessionObsolete(1,"会话过时，请重新登录"),
    NoPermission(2,"权限不足"),
    OverLimit(3,"访问太过频繁,请稍后再试"),

    // account-server
    AccountServerError(1001,"账户服务异常，请稍后再试"),
    PhoneRegistered(1002,"此手机号码已被注册"),
    AccountUpdateError(1003,"更新账户信息失败,请确认输入的参数是否正确"),
    AccountGetError(1004,"获取账户失败,请确认输入的参数是否正确"),
    PasswordLoginError(1005, "手机号码或密码错误"),
    Rejected(1006, "信用未达标，无法登陆"),
    MoneyNotEnough(1007,"余额不足"),
    PayAccountNull(1008, "转钱账户不存在"),
    ReceiveAccountNull(1009, "收钱账户不存在"),
    AccountNullError(1010,"此账户不存在"),
    PayFailure(1011,"支付失败"),
    CodeError(1012,"验证码错误"),
    PhoneError(1013,"手机号码错误"),

    // first-filter-server
    FirstFilterServerError(1011,"初筛服务异常，请稍后再试"),
    RuleUpdateError(2001,"初筛规则更新失败"),

    // gateway
    TokenError(3001, "请重新登录后再访问"),
    OauthServerError(3001, "认证服务异常，请稍后再试"),

    //kill-server
    ProductNotExist(4001,"该产品不存在"),
    AccountNotExist(4002,"该账户不存在"),
    SoldOut(4003,"该产品已售罄"),
    OutLimitedQuantity(4004,"超出该产品的限购数量"),
    KillNotTime(4005,"未到该产品抢购时间"),
    isRejected(4006,"您不符合秒杀条件，因此无法参加秒杀活动"),
    OverLimitedQuantity(4007,"不能超出限购数量!"),
    PathError(4008,"路径错误"),

    // oauth-server
    TokenOverdue(3001,"token已经过期"),
    WrongToken(3002,"非法token"),

    // order-server
    OrderServerError(5001,"产品服务异常，请稍后再试"),
    FalseOrder(5001, "订单错误"),
    DeleteOrderError(5002, "删除订单失败"),
    KillFailure(5003,"抢购失败"),

    // product-server
    ProductServerError(6005,"产品服务异常，请稍后再试"),
    ProductNullError(6005,"此产品不存在"),
    ounumber(6001,"抢购数量不在范围内"),
    AddProductERRO(6002,"增加商品失败,请确认输入的参数是否正确"),
    DeleteProductERRO(6003,"删除商品失败"),
    UpdateProductERRO(6004,"产品更新失败,请确认输入的参数是否正确"),
    UpdateProductStatusERRO(6005,"产品状态更新失败,,请确认输入的参数是否正确"),
    ParamERRO(6006,"参数格式错误"),
    SelectProductERRO(6007,"查询产品为空，亲确认输入的参数是否正确"),
    ProductNameRepeat(6008,"该产品名称已存在"),
    ProductIDRepeat(6009,"产品id重复"),
    ProductNoEnouth(60010,"产品库存不够"),
    ReduceProductERRO(6010,"产品扣减库存失败"),
    IncreaseProductERRO(6011,"产品数量增加失败"),
    KillOpenNoUpdate(6012,"秒杀开始后，无法修改产品信息");


    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    ResultCode(String successMessage) {
        this.code = 0;
        this.message = successMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

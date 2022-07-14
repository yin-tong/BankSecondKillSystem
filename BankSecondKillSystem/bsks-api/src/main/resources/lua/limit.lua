-- 为某个接口的请求设置计数器
-- KEYS[1] = key,格式为 类名.方法名:账户id
-- ARGV[1] = 限制时间
-- ARGV[2] = 限制的次数
-- 返回1 可以访问
--返回0 达到限制数量

-- key不存在则创建
if (redis.call('exists', KEYS[1]) == 0) then
    redis.call('set',KEYS[1],1);
    redis.call("expire",KEYS[1],ARGV[1]);
    return 1;
end;
-- key存在访问数量加1
local count = tonumber(redis.call('incrby', KEYS[1],1));
-- 访问数量<=限制次数
if (count <= tonumber(ARGV[2])) then
    return 1;
end
-- 访问数量 > 限购数量
return 0;

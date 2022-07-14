-- 回退库存
-- KEYS[1] = key,格式为 productQuantity:产品id
-- ARGV[1] = 回退数量
-- 返回整数 回退成功后的库存数量
-- 返回0 产品不存在

-- 产品存在，回退库存
if (redis.call('exists', KEYS[1]) == 1) then
   local num = tonumber(ARGV[1]);
   -- 返回回退成功后的库存数量
   return redis.call('incrby', KEYS[1],num);
end;
-- 产品不存在，返回0
return 0;

-- 扣减库存
-- KEYS[1] = key,格式为 productQuantity:产品id
-- ARGV[1] = 购买数量
-- 返回正整数 库存数量
-- 返回0 库存为0
-- 返回-1 购买数量>库存
-- 返回-2 产品不存在

-- 产品存在
if (redis.call('exists', KEYS[1]) == 1) then
   -- 获取库存
   local stock = tonumber(redis.call('get', KEYS[1]));
   -- 获取购买数量
   local num = tonumber(ARGV[1]);
   if (stock == 0) then
      return 0;
   end;
   if (stock < num) then
      return -1;
   end;
   -- 返回剩余库存
   return redis.call('decrby', KEYS[1],num);
end;
return -2;

local stockKey = KEYS[1]
local userBoughtKey = KEYS[2]

local quantity = tonumber(ARGV[1])

redis.call('INCRBY', stockKey, quantity)
redis.call('DECRBY', userBoughtKey, quantity)

local bought = tonumber(redis.call('GET', userBoughtKey))
if bought ~= nil and bought <= 0 then
    redis.call('DEL', userBoughtKey)
end

return 1
local stockKey = KEYS[1]
local userBoughtKey = KEYS[2]

local quantity = tonumber(ARGV[1])
local limitPerUser = tonumber(ARGV[2])

local stock = tonumber(redis.call('GET', stockKey))
if stock == nil then
    return -1
end

if stock < quantity then
    return 0
end

local bought = tonumber(redis.call('GET', userBoughtKey))
if bought == nil then
    bought = 0
end

if (bought + quantity) > limitPerUser then
    return 2
end

redis.call('DECRBY', stockKey, quantity)
redis.call('INCRBY', userBoughtKey, quantity)

return 1
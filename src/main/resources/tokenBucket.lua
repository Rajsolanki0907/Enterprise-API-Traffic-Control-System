local tokens_key = KEYS[1]
local timestamp_key = KEYS[2]

local refill_rate = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requested = tonumber(ARGV[4])

-- get current tokens
local tokens = tonumber(redis.call("GET", tokens_key))

if tokens == nil then
    tokens = capacity
end

-- get last refill timestamp
local last_refill = tonumber(redis.call("GET", timestamp_key))

if last_refill == nil then
    last_refill = now
end

-- calculate elapsed time in seconds
local elapsed = math.floor((now - last_refill) / 1000)

-- refill tokens
if elapsed > 0 then

    local refill =
            elapsed * refill_rate

    tokens =
            math.min(
                    capacity,
                    tokens + refill
            )

    last_refill =
            last_refill + (elapsed * 1000)
end

local allowed = 0

if tokens >= requested then

    tokens = tokens - requested

    allowed = 1
end

-- save updated values
redis.call("SET", tokens_key, tokens)
redis.call("SET", timestamp_key, last_refill)

redis.call("EXPIRE", tokens_key, 3600)
redis.call("EXPIRE", timestamp_key, 3600)

return allowed
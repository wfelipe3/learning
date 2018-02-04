def get_size():
    return int(input())

def get_values():
    values = input().split(' ')
    return map(int, values)

def sum_values(values):
    result = 0
    for x in values:
        result = result + x
    return result

def sum_rec(values):
    def sum_rec(values, accum):
        if not values:
            return accum
        else:
            actual = values.pop(0)
            return sum_rec(values, actual + accum)
    return sum_rec(values, 0)

get_size()
result = sum_values(get_values())
print(result)
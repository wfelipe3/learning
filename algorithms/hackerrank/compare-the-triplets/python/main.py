def get_values():
    s = input()
    return s.split(' ')

def to_ints(values):
    return list(map(lambda x: int(x), values))

def get_int_values():
    return to_ints(get_values())

def have_same_size(x, y):
    return len(x) is len(y)

def get_points(x, y):

    if not have_same_size(x, y):
        raise ValueError('not the same size')

    xpoints = 0
    ypoints = 0

    for i in range(3):
        if x[i] > y[i]:
            xpoints = xpoints + 1
        elif x[i] < y[i]:
            ypoints = ypoints + 1
    return (xpoints, ypoints)
        

(x, y)= get_points(get_int_values(), get_int_values())
print(format("{} {}".format(x, y)))

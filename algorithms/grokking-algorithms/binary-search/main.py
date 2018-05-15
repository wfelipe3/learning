def binary_search(list, item):
    low = 0
    high = len(list) - 1
    
    while low <= high:
        mid = int((low + high) / 2)
        guess = list[mid]

        if guess == item:
            return mid
        elif guess > item:
            high = mid - 1
        else:
            low = mid + 1

    return None

def binary_search_rec(list, item):
    if not list:
        return None

    low = 0
    high = len(list)
    mid = int((low + high) / 2)
    guess = list[mid]
    print(f"list: {list}, item: {item}, mid: {mid}, guess: {guess}")

    if guess == item:
        return mid
    elif guess > item:
        partial = binary_search_rec(list[low:mid], item)
        if partial is None:
            return None
        return partial
    else:
        partial = binary_search_rec(list[mid+1:high], item)
        if partial is None:
            return None
        else:
            return mid + partial + 1


def compare(search1, search2, list, item):
    b1 = search1(list, item)
    b2 = search2(list, item)
    print(f"b1: {b1}, b2: {b2}")
    return b1 is b2

my_list = [1, 3, 5, 7, 9, 10, 20, 99, 123, 568, 1002, 9865]

print(compare(binary_search, binary_search_rec, my_list, 3))
print(compare(binary_search, binary_search_rec, my_list, 7))
print(compare(binary_search, binary_search_rec, my_list, 5))
print(compare(binary_search, binary_search_rec, my_list, -1))
print(compare(binary_search, binary_search_rec, my_list, 10))
print(compare(binary_search, binary_search_rec, my_list, 80))
print(compare(binary_search, binary_search_rec, my_list, 99))
print(compare(binary_search, binary_search_rec, my_list, 20))
print(compare(binary_search, binary_search_rec, my_list, 568))
print(compare(binary_search, binary_search_rec, my_list, 1002))
print(compare(binary_search, binary_search_rec, my_list, 9865))
print(compare(binary_search, binary_search_rec, my_list, 2000))


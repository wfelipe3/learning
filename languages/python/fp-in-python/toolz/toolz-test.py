from toolz.functoolz import compose, curry, identity, memoize
from toolz.itertoolz import nth, last, drop, take, groupby, interpose, first
from toolz.dicttoolz import merge, assoc, dissoc, get_in
from assertpy import assert_that
from pyrsistent import v, pvector, pmap, m

def g(x):
    return x + 10

def f(x):
    return x * 5

def test_compose():
    assert_that(compose(f, g)(3)).is_equal_to(65)
    assert_that(compose(g, f)(3)).is_equal_to(25)

def test_compose_with_map():
    print(list(map(compose(f, g), [1, 2, 3])))
    assert_that(list(map(compose(f, g), [1, 2, 3]))).is_equal_to([55, 60, 65])

@curry
def plus(x, y):
    return x + y

def test_curry():
    assert_that(plus(1, 2)).is_equal_to(3)
    assert_that(plus(1)(2)).is_equal_to(3)
    assert_that(list(map(plus(1), [1, 2, 3]))).is_equal_to([2, 3, 4])

def test_identity():
    assert_that(list(map(identity, [1, 2, 3]))).is_equal_to([1, 2, 3])

def test_memoize():

    @memoize
    def foo(x):
        print(x)
        return x + 3

    foo(1)
    assert_that(foo(1)).is_equal_to(4)

def test_nth():
    l = list([1, 2, 3])
    i = iter([1, 2, 3])
    assert_that(nth(2, l)).is_equal_to(3)
    assert_that(nth(2, l)).is_equal_to(3)
    assert_that(nth(2, i)).is_equal_to(3) #Can do only once for the iter
    assert_that(last(l)).is_equal_to(nth(len(l) - 1, l))

def test_last_drop_take():
    l = list([1, 2, 3])
    assert_that(pvector(drop(2, l))).is_equal_to(v(3))
    assert_that(pvector(take(2, l))).is_equal_to(v(1, 2))
    assert_that(pmap(groupby(first, ['ABC', 'ABA', 'BAB', 'BAA']))).is_equal_to(m(A=['ABC', 'ABA'], B=['BAB', 'BAA']))
    assert_that(pmap(groupby(identity, ['ABC', 'ABA', 'BAB', 'BAA']))).is_equal_to(m(ABC=['ABC'], ABA=['ABA'], BAB=['BAB'], BAA=['BAA']))

def test_interpose():
    l = list([1, 2, 3])
    assert_that(pvector(interpose('foo', l))).is_equal_to(v(1, 'foo', 2, 'foo', 3))
    assert_that(','.join(['foo', 'bar'])).is_equal_to('foo,bar')

def test_dicttoolz():
    d1 = {'foo': 'bar'}
    d2 = {'baz': 'quux'}
    assert_that(merge(d1, d2)).is_equal_to({'foo': 'bar', 'baz': 'quux'})
    assert_that(d1).is_equal_to({'foo': 'bar'})
    assert_that(assoc(d1, 'a', 1)).is_equal_to({'foo': 'bar', 'a': 1})
    assert_that(dissoc(d2, 'baz')).is_equal_to({})
    struct = {'a': [{'c': 'hello'}]}
    assert_that(get_in(['a', 0, 'c'], struct)).is_equal_to(struct['a'][0]['c'])
    assert_that(get_in(['a', 0, 'd'], struct, 'not found')).is_equal_to('not found')

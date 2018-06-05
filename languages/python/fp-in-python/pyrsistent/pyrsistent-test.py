from pyrsistent import pvector, v, pmap, m, pset, s
from assertpy import assert_that

def test_pvector():
    vec = pvector([1, 2, 3])
    assert_that(vec[2]).is_equal_to(3)
    assert_that(vec).is_equal_to(v(1, 2, 3))

def test_pvector_add_creates_new_vector():
    vec = pvector([1, 2, 3])
    assert_that(vec).is_equal_to(v(1, 2, 3))
    assert_that(vec.append(4)).is_equal_to(v(1, 2, 3, 4))

def test_set_pvector():
    vec = pvector([1, 2, 3])
    assert_that(vec.set(0, 'hello')).is_equal_to(v('hello', 2, 3))

def test_pmap():
    d = pmap({'foo': 'bar', 'value': 2})
    assert_that(d).is_equal_to(m(foo='bar', value=2))

def test_pmap_get_value():
    d = pmap({'foo': 'bar', 'value': 2})
    assert_that(d['foo']).is_equal_to('bar')

def test_pmap_set_value():
    d = pmap({'foo': 'bar', 'value': 2})
    assert_that(d).is_equal_to(m(foo='bar', value=2))
    assert_that(d.set('foo', 'baz')).is_equal_to(m(foo='baz', value=2))

def test_pset():
     animals= pset(['chicken', 'pig', 'cow']) | s('donkey', 'dog')
     assert_that(animals).is_equal_to(s('chicken', 'pig', 'cow', 'donkey', 'dog'))

def test_pset_add_value():
     animals= pset(['chicken', 'pig', 'cow']) | s('donkey', 'dog')
     assert_that(animals.add('cat')).is_equal_to(s('chicken', 'pig', 'cow', 'donkey', 'dog', 'cat'))
     assert_that(animals.remove('pig')).is_equal_to(s('chicken', 'cow', 'donkey', 'dog'))

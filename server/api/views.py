from django.shortcuts import render
from django.http import HttpResponse
import json


def extract_keys(data, keys):
    out = {}
    for key in keys:
        if key in data:
            out[key] = data[key]

    return out


def extract(data, key):
    try:
        out = []
        for d in data:
            row = {}
            for subquery_element in key:
                # print("querying for: %s in %s" % (subquery_element, d))
                if type(subquery_element) == dict:
                    sub_key = next(iter(subquery_element.keys()))
                    sub_val = next(iter(subquery_element.values()))
                    row[sub_key] = extract_keys(d[sub_key], sub_val)
                else:
                    row[subquery_element] = d[subquery_element]

            out.append(row)

        return out
    except Exception as e:
        print(e)

# Create your views here.
def foo(request):

    query = json.loads(str(request.body, 'utf8'))
    print(request.body)

    test_data = {
        "~:app/posts": [
            {"~:user": {"~:username": "David", "~:age": 23}, "~:id": 1, "~:content": "Hello iOS meetup!"},
            {"~:user": {"~:username": "Fred", "~:age": 15}, "~:id": 2, "~:content": "Hi I'm Fred"},
            {"~:user": {"~:username": "Paul", "~:age": 100}, "~:id": 3, "~:content": "Anyone wanna hang out?"},
            {"~:user": {"~:username": "Jiyoon", "~:age": 30}, "~:id": 4, "~:content": "What is this app?"},
            {"~:user": {"~:username": "Bob", "~:age": 20}, "~:id": 5, "~:content": "Hey guys!"},
            {"~:user": {"~:username": "Mark", "~:age": 15}, "~:id": 6, "~:content": "iOS rocks"},
            {"~:user": {"~:username": "Jiwon", "~:age": 22}, "~:id": 7, "~:content": "I think android is better"},
            {"~:user": {"~:username": "Dude", "~:age": 38}, "~:id": 8, "~:content": "I am on the server :)"},
        ],
    }

    out = {}
    for queryset in query["~:remote"]:
        for target, subquery in queryset.items():
            print("Client is requesting %s inside %s" % (subquery, target))
            out[target] = extract(test_data[target], subquery)

    response = HttpResponse(json.dumps(out))
    response['Content-Type'] = "application/json"
    return response

from django.shortcuts import render
from django.http import HttpResponse
import json

# Create your views here.
def foo(request):

    query = json.loads(str(request.body, 'utf8'))
    print(request.body)
    #query = json.loads('{"~:remote":[{"~:app/people":["~:username","~:age"]}]}')

    test_data = {
        "~:app/posts": [
            {"~:username": "David", "~:id": 1, "~:content": "Hello iOS meetup!"},
            {"~:username": "Fred", "~:id": 2, "~:content": "Hi I'm Fred"},
            {"~:username": "Paul", "~:id": 3, "~:content": "Anyone wanna hang out?"},
            {"~:username": "Jiyoon", "~:id": 4, "~:content": "What is this app?"},
            {"~:username": "Bob", "~:id": 5, "~:content": "Hey guys!"},
            {"~:username": "Mark", "~:id": 6, "~:content": "iOS rocks"},
            {"~:username": "Jiwon", "~:id": 7, "~:content": "I think android is better"},
            {"~:username": "Dude", "~:id": 8, "~:content": "I am on the server :)"},
        ],
    }

    out = {}
    for queryset in query["~:remote"]:
        for target, subquery in queryset.items():
            print("Client is requesting %s inside %s" % (subquery, target))
            out[target] = []
            for data in test_data[target]:
                row = {}
                for subquery_element in subquery:
                    row[subquery_element] = data[subquery_element]
                out[target].append(row)


    response = HttpResponse(json.dumps(out))
    response['Content-Type'] = "application/json"
    return response

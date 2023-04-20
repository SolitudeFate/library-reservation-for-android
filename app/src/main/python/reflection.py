import time

def room_to_gsid(room):
    mapping = {
        '201': '3fb64d94-0901-4683-be56-a9ada9804399',
        '205': '3fb64d94-0901-4683-be56-a9ada9804399',
        '232': 'c710ea4b-91ae-4a9d-a3f2-ca7e31460b68',
        '331': 'bca546ce-1e6c-4b4d-9e45-a20aba076687',
        '332': '601a3d02-4696-49e1-b9c6-120f0ea59465',
        '302': '69eb5d3c-34a8-4b15-9a0d-01b3cc02f29c',
        '306': '69eb5d3c-34a8-4b15-9a0d-01b3cc02f29c',
        '431': '5388800a-4e31-4b23-92d7-f767c6f63c05',
        '432': 'd989e223-e0ce-4375-90ba-8d010f8b0735'
    }

    gsid = mapping[room]
    return gsid

def get_month():
    month = time.localtime(time.time() + 86400).tm_mon
    month = str(month)
    return month

def get_day():
    day = time.localtime(time.time() + 86400).tm_mday
    day = str(day)
    return day
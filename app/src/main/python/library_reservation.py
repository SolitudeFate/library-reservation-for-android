import requests
import time

# 预约明天的座位
def reserve(room, zwid, cookie):
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
    starttime = '8:00'
    endtime = '22:00'

    # 获取明天日期
    month = time.localtime(time.time() + 86400).tm_mon
    day = time.localtime(time.time() + 86400).tm_mday

    url = "https://bgweixin.sspu.edu.cn/app/readroom/ylsyySave.do"

    headers = {
        'Cookie': cookie,
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6309001c) XWEB/6500',
        'Referer': 'https://bgweixin.sspu.edu.cn/app/readroom/index.do'
    }

    data = {
        'gsid': gsid,
        'zwid': zwid,
        'day': day,
        'month': month,
        'starttime': starttime,
        'endtime': endtime
    }

    # 避免警告
    requests.packages.urllib3.disable_warnings()
    # verify关闭校验
    response = requests.post(url, headers=headers, data=data, verify=False)

    return response.text
import os
import time
import pika
import json
import argparse
import requests
from datetime import datetime
from random import randint
clear = lambda: os.system('clear')

#CONFIG
API_ENDPOINT = "https://jsonplaceholder.typicode.com/posts"

arguments = argparse.ArgumentParser(description="",add_help=False, allow_abbrev=False)

arguments.add_argument("--host", help="Name Host of Rabbit")
arguments.add_argument("--exchange", help="Name Exchange of Rabbit")
arguments.add_argument("--device", help="Device of temp")

args_parsed = arguments.parse_args()

DEVICE_ID = args_parsed.device
connection = pika.BlockingConnection(pika.ConnectionParameters(host=args_parsed.host))

while True:
        wait = randint(1, 2)
        for i in range(wait, 0, -1):
                clear()
                print("The next information will be sent in %s seconds" % i)
                time.sleep(1)
        data = {'IdDispositivo': DEVICE_ID, 'fechaGeneracion': datetime.now().strftime("%Y-%m-%d %H:%M:%S"), 'temperatura': randint(-100, 100), 'humedad': randint(-100, 100)}

        channel = connection.channel()
        channel.exchange_declare(exchange=args_parsed.exchange)
        channel.queue_declare(queue='dev-temp')
        channel.queue_bind(queue='dev-temp', exchange=args_parsed.exchange, routing_key='*')
        channel.basic_publish(exchange=args_parsed.exchange, routing_key='*', body=json.dumps(data))
#         connection.close()

        # r = requests.post(url=API_ENDPOINT, data=data)
        # response = r.text
        # print("The response is:%s" % response)
        time.sleep(1)
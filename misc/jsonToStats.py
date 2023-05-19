import json
from datetime import datetime, timedelta

# Read the JSON file
with open('test.json') as f:
    json_data = json.load(f)

# Convert the JSON data to lists of datetimes
lists = []
for json_list in json_data:
    dt_list = [datetime.strptime(dt_str, "%H:%M:%S") for dt_str in json_list]
    lists.append(dt_list)

# Use the lists in your Python program
#for lst in lists:
#    print(lst)

def calcQueue():
    print("Calculating queue...")

def calcOrder():
    print("Calculating order...")

def calcPickupQueue():
    print("Calculating pickup queue...")

def calcPickup():
    print("Calculating pickup...")
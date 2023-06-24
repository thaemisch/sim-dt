import json
from datetime import datetime, timedelta

filename = input("Enter the filename: ")

# Read the JSON file
with open(filename) as f:
    json_data = json.load(f)

# Convert the JSON data to lists of datetimes
lists = []
for json_list in json_data:
    dt_list = [datetime.strptime(dt_str, "%H:%M:%S") for dt_str in json_list]
    lists.append(dt_list)

queue = lists[0]
order = lists[1]
pickup_queue = lists[2]
pickup = lists[3]
exit = lists[4]

queue_deltas = []
order_deltas = []
pickup_queue_deltas = []
pickup_deltas = []

sum_queue_deltas = timedelta(0)
sum_order_deltas = timedelta(0)
sum_pickup_queue_deltas = timedelta(0)
sum_pickup_deltas = timedelta(0)

# Calculate Queue
for i in range(len(queue)):
    queue_deltas.append(order[i] - queue[i])
max_queue_delta = max(queue_deltas)
min_queue_delta = min(queue_deltas)
for i in range(len(queue_deltas)):
    sum_queue_deltas += queue_deltas[i]
avg_queue_delta = sum_queue_deltas / len(queue_deltas)

# Calculate Order
for i in range(len(order)):
    order_deltas.append(pickup_queue[i] - order[i])
max_order_delta = max(order_deltas)
min_order_delta = min(order_deltas)
for i in range(len(order_deltas)):
    sum_order_deltas += order_deltas[i]
avg_order_delta = sum_order_deltas / len(order_deltas)

# Calculate Pickup Queue
for i in range(len(pickup_queue)):
    pickup_queue_deltas.append(pickup[i] - pickup_queue[i])
max_pickup_queue_delta = max(pickup_queue_deltas)
min_pickup_queue_delta = min(pickup_queue_deltas)
for i in range(len(pickup_queue_deltas)):
    sum_pickup_queue_deltas += pickup_queue_deltas[i]
avg_pickup_queue_delta = sum_pickup_queue_deltas / len(pickup_queue_deltas)

# Calculate Pickup
for i in range(len(pickup)):
    pickup_deltas.append(exit[i] - pickup[i])
max_pickup_delta = max(pickup_deltas)
min_pickup_delta = min(pickup_deltas)
for i in range(len(pickup_deltas)):
    sum_pickup_deltas += pickup_deltas[i]
avg_pickup_delta = sum_pickup_deltas / len(pickup_deltas)

print("----------------------------------------")
print("Queue")
print("Max: " + str(max_queue_delta))
print("Min: " + str(min_queue_delta))
print("Avg: " + str(avg_queue_delta))
print("----------------------------------------")
print("Order")
print("Max: " + str(max_order_delta))
print("Min: " + str(min_order_delta))
print("Avg: " + str(avg_order_delta))
print("----------------------------------------")
print("Pickup Queue")
print("Max: " + str(max_pickup_queue_delta))
print("Min: " + str(min_pickup_queue_delta))
print("Avg: " + str(avg_pickup_queue_delta))
print("----------------------------------------")
print("Pickup")
print("Max: " + str(max_pickup_delta))
print("Min: " + str(min_pickup_delta))
print("Avg: " + str(avg_pickup_delta))
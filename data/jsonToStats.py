import json
from datetime import datetime, timedelta
import markdown

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
queue_deltas_sideways = []
order_deltas = []
pickup_queue_deltas = []
pickup_deltas = []

# Calculate min/max of new arrivals
for i in range(len(queue)-1):
    queue_deltas_sideways.append(queue[i+1] - queue[i])
max_queue_delta_sideways = max(queue_deltas_sideways)
min_queue_delta_sideways = min(queue_deltas_sideways)

# Calculate min/max of each station
# Queue
for i in range(len(queue)):
    queue_deltas.append(order[i] - queue[i])
max_queue_delta = max(queue_deltas)
min_queue_delta = min(queue_deltas)

# Order
for i in range(len(order)):
    order_deltas.append(pickup_queue[i] - order[i])
max_order_delta = max(order_deltas)
min_order_delta = min(order_deltas)

# Pickup Queue
for i in range(len(pickup_queue)):
    pickup_queue_deltas.append(pickup[i] - pickup_queue[i])
max_pickup_queue_delta = max(pickup_queue_deltas)
min_pickup_queue_delta = min(pickup_queue_deltas)

# Pickup
for i in range(len(pickup)):
    pickup_deltas.append(exit[i] - pickup[i])
max_pickup_delta = max(pickup_deltas)
min_pickup_delta = min(pickup_deltas)

# Print results
print("New arraival:")
print("Min: " + str(min_queue_delta_sideways))
print("Max: " + str(max_queue_delta_sideways))
print("--------------------")
print("Queue:")
print("Min: " + str(min_queue_delta))
print("Max: " + str(max_queue_delta))
print("--------------------")
print("Order:")
print("Min: " + str(min_order_delta))
print("Max: " + str(max_order_delta))
print("--------------------")
print("Pickup Queue:")
print("Min: " + str(min_pickup_queue_delta))
print("Max: " + str(max_pickup_queue_delta))
print("--------------------")
print("Pickup:")
print("Min: " + str(min_pickup_delta))
print("Max: " + str(max_pickup_delta))

# Create a markdown string with the variables and descriptions as a table
markdown_string = f"""| Legend | Value |
| --- | --- |
| New arrival: Min | {min_queue_delta_sideways} |
| New arrival: Max | {max_queue_delta_sideways} |
| Queue: Min | {min_queue_delta} |
| Queue: Max | {max_queue_delta} |
| Order: Min | {min_order_delta} |
| Order: Max | {max_order_delta} |
| Pickup Queue: Min | {min_pickup_queue_delta} |
| Pickup Queue: Max | {max_pickup_queue_delta} |
| Pickup: Min | {min_pickup_delta} |
| Pickup: Max | {max_pickup_delta} |
"""

# Write the markdown string to a file
with open("README.md", "w") as f:
    f.write(markdown.markdown(markdown_string))